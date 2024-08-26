package com.newnomal.booth_reservation.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityResponse {
    private Integer boothStartTimezone;
    private Integer boothEndTimezone;
    private Integer maxTimeZoneNumber;
    private Integer totalReservedTimeZones;
    private List<List<Integer>> reservedTimeZones;
    private Integer remainingTimeZones;
}