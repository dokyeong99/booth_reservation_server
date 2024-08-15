package com.newnomal.booth_reservation.domain.request;

import com.newnomal.booth_reservation.common.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String nickname;//유저의 이름
    private String profileImage;//kakao_profile_image
    private String secretPassword;//분실물 되찾기에 필요한 부스 잠금해제 개인비밀번호 => 초기 비밀번호 1234
    private Long kakaoId;//OAUTH를 통해 얻어온 KAKAO ID => 현재는 LongType의 ID가 오지만 차후에 UUID로 변환할 필요 있음

}
