package com.newnomal.booth_reservation.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityRequest {
    private String name;
    private String description;
    private String image;
    private Float latitude;
    private Float longitude;
    private Integer boothStartTimezone;
    private Integer boothEndTimezone;
    private Integer maxTimeZoneNumber;
    private String weekHolidays;
}