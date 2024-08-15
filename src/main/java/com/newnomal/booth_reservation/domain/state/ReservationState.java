package com.newnomal.booth_reservation.domain.state;

public enum ReservationState {
    VALID,//예약 완료
    CANCELED,//예약 취소
    REJECTED,//예약 거절
    COMPLETED//이용 정상 종료
}
