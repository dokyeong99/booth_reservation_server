package com.newnomal.booth_reservation.domain.response;

import com.newnomal.booth_reservation.domain.entity.Authority;
import com.newnomal.booth_reservation.domain.state.AuthorityState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityResponse {
    private Long id;
    private String name;
    private String description;
    private String image;
    private Float latitude;
    private Float longitude;
    private Integer boothStartTimezone;
    private Integer boothEndTimezone;
    private Integer maxTimeZoneNumber;
    private AuthorityState status;
    private String weekHolidays;

    public AuthorityResponse(Authority authority) {
        this.id = authority.getId();
        this.name = authority.getName();
        this.description = authority.getDescription();
        this.image = authority.getImage();
        this.latitude = authority.getLatitude();
        this.longitude = authority.getLongitude();
        this.boothStartTimezone = authority.getBoothStartTimezone();
        this.boothEndTimezone = authority.getBoothEndTimezone();
        this.maxTimeZoneNumber = authority.getMaxTimeZoneNumber();
        this.status = authority.getStatus();
        this.weekHolidays = authority.getWeekHolidays();
    }
}