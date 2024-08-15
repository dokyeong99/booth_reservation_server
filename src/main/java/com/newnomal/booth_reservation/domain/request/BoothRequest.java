package com.newnomal.booth_reservation.domain.request;

import com.newnomal.booth_reservation.domain.state.BoothState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoothRequest {
    private Long authorityId;
    private Integer boothNumber;
    private BoothState state;
}