package com.newnomal.booth_reservation.domain.response;

import com.newnomal.booth_reservation.domain.entity.Holiday;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HolidayResponse {
    private Long id;
    private LocalDate date;

    public HolidayResponse(Holiday holiday) {
        this.id = holiday.getId();
        this.date = holiday.getDate();
    }
}