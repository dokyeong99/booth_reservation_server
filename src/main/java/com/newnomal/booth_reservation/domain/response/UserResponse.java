package com.newnomal.booth_reservation.domain.response;

import com.newnomal.booth_reservation.common.Role;
import com.newnomal.booth_reservation.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String nickname;//유저의 이름
    private String profileImage;//kakao_profile_image
    private String secretPassword;//분실물 되찾기에 필요한 부스 잠금해제 개인비밀번호 => 초기 비밀번호 1234
    private Long kakaoId;//OAUTH를 통해 얻어온 KAKAO ID => 현재는 LongType의 ID가 오지만 차후에 UUID로 변환할 필요 있음
    private Role role;

    public UserResponse(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
        this.secretPassword = user.getSecretPassword();
        this.kakaoId = user.getKakaoId();
        this.role = user.getRole();
    }
}
