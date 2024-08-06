package com.newnomal.booth_reservation.common;
import lombok.*;

//토큰에 저장되는 정보
@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class TokenInfo {
    private Long id;
    private String nickname;//유저의 이름
    private Role role;
}
