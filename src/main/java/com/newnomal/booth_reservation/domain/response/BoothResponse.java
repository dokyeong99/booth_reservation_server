package com.newnomal.booth_reservation.domain.response;

import com.newnomal.booth_reservation.domain.entity.Booth;
import com.newnomal.booth_reservation.domain.state.BoothState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoothResponse {
    private Long id;
    private Long authorityId;
    private Integer boothNumber;
    private BoothState state;
    private Long version;

    public BoothResponse(Booth booth) {
        this.id = booth.getId();
        this.authorityId = booth.getAuthorityId();
        this.boothNumber = booth.getBoothNumber();
        this.state = booth.getState();
        this.version = booth.getVersion();
    }
}