package com.newnomal.booth_reservation.service;

import com.newnomal.booth_reservation.common.RestResult;
import com.newnomal.booth_reservation.domain.entity.Authority;
import com.newnomal.booth_reservation.domain.entity.Booth;
import com.newnomal.booth_reservation.domain.entity.Reservation;
import com.newnomal.booth_reservation.domain.request.BoothRequest;
import com.newnomal.booth_reservation.domain.response.AvailabilityResponse;
import com.newnomal.booth_reservation.domain.response.BoothResponse;
import com.newnomal.booth_reservation.domain.state.BoothState;
import com.newnomal.booth_reservation.repository.AuthorityRepository;
import com.newnomal.booth_reservation.repository.BoothRepository;
import com.newnomal.booth_reservation.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoothService {
    private final BoothRepository boothRepository;
    private final AuthorityRepository authorityRepository;
    private final ReservationRepository reservationRepository;

    public ResponseEntity<RestResult<Object>> createBooth(BoothRequest boothRequest) {
        Booth booth = new Booth();
        booth.setAuthorityId(boothRequest.getAuthorityId());
        booth.setBoothNumber(boothRequest.getBoothNumber());
        booth.setState(BoothState.VALID);
        booth = boothRepository.save(booth);
        return ResponseEntity.ok(new RestResult<>("SUCCESS", new BoothResponse(booth)));
    }

    public ResponseEntity<RestResult<Object>> getBooth(Long id) {
        Optional<Booth> booth = boothRepository.findById(id);
        if (booth.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RestResult<>("NOT_FOUND", new RestResult<>("BOOTH_NOT_FOUND", "존재하지 않는 부스입니다")));
        }
        return ResponseEntity.ok(new RestResult<>("SUCCESS", new BoothResponse(booth.get())));
    }

    public ResponseEntity<RestResult<Object>> getBoothsByAuthority(Long authorityId) {
        List<Booth> booths = boothRepository.findByAuthorityIdStateVAlid(authorityId);
        List<BoothResponse> boothResponses = booths.stream()
                .map(BoothResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new RestResult<>("SUCCESS", boothResponses));
    }

    public ResponseEntity<RestResult<Object>> updateBooth(Long id, BoothRequest boothRequest) {
        Optional<Booth> boothOpt = boothRepository.findById(id);
        if (boothOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RestResult<>("NOT_FOUND", new RestResult<>("BOOTH_NOT_FOUND", "존재하지 않는 부스입니다")));
        }
        Booth booth = boothOpt.get();
        booth.setBoothNumber(boothRequest.getBoothNumber());
        booth.setState(boothRequest.getState());
        booth = boothRepository.save(booth);
        return ResponseEntity.ok(new RestResult<>("SUCCESS", new BoothResponse(booth)));
    }

    public ResponseEntity<RestResult<Object>> deleteBooth(Long id) {
        Optional<Booth> boothOpt = boothRepository.findById(id);
        if (boothOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RestResult<>("NOT_FOUND", new RestResult<>("BOOTH_NOT_FOUND", "존재하지 않는 부스입니다")));
        }
        Booth booth = boothOpt.get();
        booth.setState(BoothState.DELETED);
        boothRepository.save(booth);
        return ResponseEntity.ok(new RestResult<>("SUCCESS", "부스가 삭제되었습니다"));
    }

    public ResponseEntity<RestResult<Object>> getBoothAvailability(Long authorityId, Long userId, LocalDate date) {
        Optional<Authority> authorityOpt = authorityRepository.findById(authorityId);
        if (authorityOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RestResult<>("NOT_FOUND", new RestResult<>("AUTHORITY_NOT_FOUND", "존재하지 않는 기관입니다")));
        }
        Authority authority = authorityOpt.get();

        List<Reservation> userReservations = reservationRepository.findByUserIdAndAuthorityIdAndReservationDate(userId, authorityId, date);

        int totalReservedTimeZones = userReservations.stream()
                .mapToInt(reservation -> reservation.getReservationEndTimeZone() - reservation.getReservationStartTimeZone() + 1)
                .sum();

        int remainingTimeZones = authority.getMaxTimeZoneNumber() - totalReservedTimeZones;

        AvailabilityResponse response = new AvailabilityResponse(
                authority.getBoothStartTimezone(),
                authority.getBoothEndTimezone(),
                authority.getMaxTimeZoneNumber(),
                totalReservedTimeZones,
                remainingTimeZones
        );

        return ResponseEntity.ok(new RestResult<>("SUCCESS", response));
    }



    public ResponseEntity<RestResult<Object>> getReservedTimeZone(
             Long boothId, LocalDate date) {
        List<Reservation> reservations = reservationRepository.findAllByBoothIdAndReservationDate(boothId,date);
        List<Integer> reservedTImeZoneList = new ArrayList<>();
        reservations.forEach(reservation -> addReservedTimeZone(reservedTImeZoneList, reservation));
        return ResponseEntity.ok(new RestResult<>("SUCCESS",reservedTImeZoneList));
    }

    private void addReservedTimeZone(List<Integer> arr, Reservation reservation){
        Integer startTimeZone = reservation.getReservationStartTimeZone();
        Integer endTimeZone = reservation.getReservationEndTimeZone();
        for (int i = startTimeZone; i <= endTimeZone; i++) {
            arr.add(i);
        }
    }
}