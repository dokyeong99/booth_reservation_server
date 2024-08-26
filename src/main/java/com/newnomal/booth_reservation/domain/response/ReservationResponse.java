package com.newnomal.booth_reservation.domain.response;

import com.newnomal.booth_reservation.domain.entity.Reservation;
import com.newnomal.booth_reservation.domain.state.ReservationState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    private Long id;
    private Long boothId;
    private Long userId;
    private Integer reservationStartTimeZone;
    private Integer reservationEndTimeZone;
    private String qrIdentifier;
    private LocalDate reservationDate;
    private ReservationState state;
    private String qrCodeImage;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.boothId = reservation.getBoothId();
        this.userId = reservation.getUserId();
        this.reservationStartTimeZone = reservation.getReservationStartTimeZone();
        this.reservationEndTimeZone = reservation.getReservationEndTimeZone();
        this.qrIdentifier = reservation.getQrIdentifier();
        this.reservationDate = reservation.getReservationDate();
        this.state = reservation.getState();
    }
}