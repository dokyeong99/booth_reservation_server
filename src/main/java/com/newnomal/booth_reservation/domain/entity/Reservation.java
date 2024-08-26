package com.newnomal.booth_reservation.domain.entity;

import com.newnomal.booth_reservation.domain.state.ReservationState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "reservation_table", indexes ={
        @Index(name = "boothIndex",columnList = "boothId"),
        @Index(name = "userIndex",columnList = "userId"),
        @Index(name = "reservationDateIndex",columnList = "reservationDate")
})
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long boothId;
    private Long userId;
    private Long authorityId;
    private Integer reservationStartTimeZone;//예약 시작 time zone(1부터 시작, 1이 00:00~00:15)
    private Integer reservationEndTimeZone;//예약 종료 time zone
    private String qrIdentifier;//QR Data
    private LocalDate reservationDate;//예약 날자
    private ReservationState state;//1.부스 이용 가능 상태 2.부스 이용 불가능 상태 3.부스 삭제 상태

}

