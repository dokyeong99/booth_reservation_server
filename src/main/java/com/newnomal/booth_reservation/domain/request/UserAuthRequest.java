package com.newnomal.booth_reservation.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAuthRequest {//회원가입, 로그인 Request 객체
    private String nickname;//유저의 이름
    private String profileImage;//kakao_profile_image
    private Long kakaoId;//OAUTH를 통해 얻어온 KAKAO ID => 현재는 LongType의 ID가 오지만 차후에 UUID로 변환할 필요 있음

}
