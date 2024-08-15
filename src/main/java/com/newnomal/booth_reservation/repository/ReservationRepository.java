package com.newnomal.booth_reservation.repository;

import com.newnomal.booth_reservation.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);

    //해당 시간 예약 가능한지 확인하는 naming query
    //select * from Reservation
    //where boothId = {boothId} and reservationDate = {reservationDate}
    //and reservationStartTimeZone <= {endTimeZone}
    //and reservationEndTimeZone >= {startTimeZone}
    boolean existsByBoothIdAndReservationDateAndReservationStartTimeZoneLessThanEqualAndReservationEndTimeZoneGreaterThanEqual(
            Long boothId, LocalDate reservationDate, Integer endTimeZone, Integer startTimeZone);


    List<Reservation> findByUserIdAndAuthorityIdAndReservationDate(Long userId, Long authorityId, LocalDate reservationDate);

    List<Reservation> findAllByBoothIdAndReservationDate(Long boothId,LocalDate date);

}
