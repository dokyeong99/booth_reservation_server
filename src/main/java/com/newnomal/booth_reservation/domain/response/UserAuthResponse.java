package com.newnomal.booth_reservation.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAuthResponse {
    private String accessToken;
    private String refreshToken;
}
