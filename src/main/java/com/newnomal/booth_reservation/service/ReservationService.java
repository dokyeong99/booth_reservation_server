package com.newnomal.booth_reservation.service;

import com.google.zxing.WriterException;
import com.newnomal.booth_reservation.common.RestResult;
import com.newnomal.booth_reservation.common.TokenInfo;
import com.newnomal.booth_reservation.config.JwtService;
import com.newnomal.booth_reservation.domain.entity.Authority;
import com.newnomal.booth_reservation.domain.entity.Booth;
import com.newnomal.booth_reservation.domain.entity.Reservation;
import com.newnomal.booth_reservation.domain.request.ReservationRequest;
import com.newnomal.booth_reservation.domain.response.ReservationResponse;
import com.newnomal.booth_reservation.domain.state.ReservationState;
import com.newnomal.booth_reservation.repository.AuthorityRepository;
import com.newnomal.booth_reservation.repository.BoothRepository;
import com.newnomal.booth_reservation.repository.ReservationRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BoothRepository boothRepository;
    private final AuthorityRepository authorityRepository;
    private final QRService qrService;
    private final JwtService jwtService;

    @Transactional
    public ResponseEntity<RestResult<Object>> createReservation(ReservationRequest reservationRequest) {
        try {
            Optional<Booth> boothOpt = boothRepository.findById(reservationRequest.getBoothId());
            if (boothOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new RestResult<>("NOT_FOUND", new RestResult<>("BOOTH_NOT_FOUND", "존재하지 않는 부스입니다")));
            }
            Booth booth = boothOpt.get();


            // 부스가 이미 예약되어 있는지 확인
            // => 프론트에서 예약이 안되어 있어 예약 요청을 하였지만
            // 그 이전에 다른 유저가 예약해 있을 가능성 있기 때문에 확인이 필요함
            // 10:00 ~ 10:30을 예약하고자 할 때, 데이터베이스에
            // 10:15 ~ 10:45 혹은 09:45 ~ 09:15가 있다면 false 반환
            boolean isConflict = reservationRepository.existsByBoothIdAndReservationDateAndReservationStartTimeZoneLessThanEqualAndReservationEndTimeZoneGreaterThanEqual(
                    reservationRequest.getBoothId(),
                    reservationRequest.getReservationDate(),
                    reservationRequest.getReservationEndTimeZone(),
                    reservationRequest.getReservationStartTimeZone()
            );

            if (isConflict) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new RestResult<>("CONFLICT", new RestResult<>("TIME_CONFLICT", "해당 시간에 이미 예약이 있습니다.")));
            }



            Reservation reservation = new Reservation();
            reservation.setAuthorityId(reservationRequest.getAuthorityId());
            reservation.setBoothId(reservationRequest.getBoothId());
            reservation.setUserId(reservationRequest.getUserId());
            reservation.setReservationStartTimeZone(reservationRequest.getReservationStartTimeZone());
            reservation.setReservationEndTimeZone(reservationRequest.getReservationEndTimeZone());
            reservation.setReservationDate(reservationRequest.getReservationDate());
            reservation.setState(ReservationState.ACTIVE);
            reservation.setQrIdentifier(UUID.randomUUID().toString());

            // OptimisticLockException 을 위해 부스 버전 증가
            booth.incrementReservationCount();
            boothRepository.save(booth);

            byte[] qrCodeImage = qrService.generateQRCode(reservation.getQrIdentifier(), 250, 250);
            String base64QRCode = Base64.getEncoder().encodeToString(qrCodeImage);

            reservation = reservationRepository.save(reservation);
            ReservationResponse response = new ReservationResponse(reservation);
            response.setQrCodeImage(base64QRCode);
            return ResponseEntity.ok(new RestResult<>("SUCCESS", response));
        } catch (OptimisticLockException e) {
            //동시성 발생
            //동시성 이벤트 발생 시에 프론트에서 한번 더 재요청 하도록 로직 구성해야함
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new RestResult<>("CONFLICT", new RestResult<>("VERSION_CONFLICT", "예약이 겹쳤습니다. 다시 시도해주세요.")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (WriterException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RestResult<>("ERROR", "QR코드 생성 실패"));
        }

    }

    //예약 정보 반환
    public ResponseEntity<RestResult<Object>> getReservation(Long id) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        if (reservationOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new RestResult<>("CONFLICT", new RestResult<>("NOT_FOUND_RESERVATION", "예약 정보가 없습니다. 다시 시도해주세요.")));
        }

        Reservation reservation = reservationOpt.get();
        ReservationResponse response = new ReservationResponse(reservation);

        try {
            byte[] qrCodeImage = qrService.generateQRCode(reservation.getQrIdentifier(), 250, 250);
            String base64QRCode = Base64.getEncoder().encodeToString(qrCodeImage);
            response.setQrCodeImage(base64QRCode);
        } catch (WriterException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RestResult<>("ERROR", "QR코드 생성 실패"));
        }

        return ResponseEntity.ok(new RestResult<>("SUCCESS", response));
    }

    //특정 유저의 예약 정보 반환
    public ResponseEntity<RestResult<Object>> getUserReservations(Long userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        List<ReservationResponse> reservationResponses = reservations.stream()
                .map(reservation -> {
                    ReservationResponse response = new ReservationResponse(reservation);
                    try {
                        byte[] qrCodeImage = qrService.generateQRCode(reservation.getQrIdentifier(), 250, 250);
                        String base64QRCode = Base64.getEncoder().encodeToString(qrCodeImage);
                        response.setQrCodeImage(base64QRCode);
                    } catch (WriterException | IOException e) {
                        // 로그를 남기고 QR 코드 없이 진행
                        // 실제 상황에 따라 에러 처리 방식을 선택하세요
                        e.printStackTrace();
                    }
                    return response;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(new RestResult<>("SUCCESS", reservationResponses));
    }

    //예약 취소
    public ResponseEntity<RestResult<Object>> cancelReservation(Long id) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        if (reservationOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RestResult<>("NOT_FOUND", new RestResult<>("RESERVATION_NOT_FOUND", "존재하지 않는 예약입니다")));
        }
        Reservation reservation = reservationOpt.get();
        reservation.setState(ReservationState.CANCELED);
        reservationRepository.save(reservation);
        return ResponseEntity.ok(new RestResult<>("SUCCESS", "예약이 취소되었습니다"));
    }

    public ResponseEntity<RestResult<Object>> verifyQRCode(String token, String qrIdentifier, LocalDate date, Integer timeZone) {
        TokenInfo tokenInfo = jwtService.extractUser(token);
        Optional<Reservation> optionalReservation = reservationRepository.findByQrIdentifierAndReservationDateAndReservationStartTimeZoneGreaterThanEqualAndReservationEndTimeZoneLessThanEqual(qrIdentifier, date, timeZone, timeZone);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            if(reservation.getId().equals(tokenInfo.getId())){
                return ResponseEntity.ok(new RestResult<>("SUCCESS", reservation));
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new RestResult<>("NOT_FOUND", new RestResult<>("RESERVATION_NOT_FOUND", "예약자가 동일하지 않습니다")));
            }
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RestResult<>("NOT_FOUND", new RestResult<>("RESERVATION_NOT_FOUND", "존재하지 않는 예약입니다")));
        }
    }
}