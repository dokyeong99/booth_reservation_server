package com.newnomal.booth_reservation.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {
    private Long boothId;
    private Long userId;
    private Integer reservationStartTimeZone;
    private Integer reservationEndTimeZone;
    private LocalDate reservationDate;
    private Long boothVersion;
}