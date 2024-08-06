package com.newnomal.booth_reservation.service;


import com.newnomal.booth_reservation.common.Role;
import com.newnomal.booth_reservation.common.TokenInfo;
import com.newnomal.booth_reservation.config.JwtService;
import com.newnomal.booth_reservation.common.RestResult;
import com.newnomal.booth_reservation.domain.entity.User;
import com.newnomal.booth_reservation.domain.request.UserAuthRequest;
import com.newnomal.booth_reservation.domain.response.UserAuthResponse;
import com.newnomal.booth_reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;


    //kakaoID기준 아이디 중복 체크하고 없다면 User 생성 이후 토큰 반환
    public ResponseEntity<RestResult<Object>> signUp(UserAuthRequest request) {
        //kakaoId 중복 체크
        Optional<User> byKakaoId = userRepository.findByKakaoId(request.getKakaoId());
        if(byKakaoId.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new RestResult<>("CONFLICT",new RestResult<>("EMAIL_CONFLICT","이미 존재하는 이메일 입니다.")));
        }

        //회원가입 완료
        User user = userRepository.save(new User(request));

        //토큰에 삽입할 정보 생성
        Map<String, Object> extraClaims = getExtraClaims(user.getId(), user.getNickname(), user.getRole());

        //토큰 생성
        String accessToken = jwtService.generateAccessToken(extraClaims);
        String refreshToken = jwtService.generateAccessToken(extraClaims);

        //Response 생성
        UserAuthResponse userAuthResponse = new UserAuthResponse(accessToken, refreshToken);

        //OK Status, Response return
        return ResponseEntity.ok(new RestResult<>("SUCCESS", userAuthResponse));
    }

    //refresh토큰까지 만료되어 다시 oauth로 로그인하여 토큰 반환하는 경우
    public ResponseEntity<RestResult<Object>> oauthLogin(UserAuthRequest request) {
        Optional<User> byKakaoId = userRepository.findByKakaoId(request.getKakaoId());
        if(byKakaoId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new RestResult<>("CONFLICT",new RestResult<>("USER_NULL_CONFLICT","존재하지 않는 유저입니다")));
        }

        //유저 정보 반환
        User user = byKakaoId.get();

        //토큰에 삽입할 정보 생성
        Map<String, Object> extraClaims = getExtraClaims(user.getId(), user.getNickname(), user.getRole());

        //토큰 생성
        String accessToken = jwtService.generateAccessToken(extraClaims);
        String refreshToken = jwtService.generateAccessToken(extraClaims);

        //Response 생성
        UserAuthResponse userAuthResponse = new UserAuthResponse(accessToken, refreshToken);

        //OK Status, Response return
        return ResponseEntity.ok(new RestResult<>("SUCCESS", userAuthResponse));
    }

    //Access토큰이 만료되어 재발급 하는 경우
    public ResponseEntity<RestResult<Object>> refreshExpiredToken(String token) {
        //토큰 정보 받아오기
        TokenInfo tokenInfo = jwtService.extractUser(token);

        //토큰에 삽입할 정보 생성
        Map<String, Object> extraClaims = getExtraClaims(tokenInfo.getId(),tokenInfo.getNickname(),tokenInfo.getRole());

        //토큰 생성
        String accessToken = jwtService.generateAccessToken(extraClaims);

        //Response 생성
        UserAuthResponse userAuthResponse = new UserAuthResponse(accessToken, null);

        return ResponseEntity.ok(new RestResult<>("SUCCESS", userAuthResponse));
    }

    //토큰에 들어갈 정보 생성
    private Map<String, Object> getExtraClaims(Long id, String nickname, Role role){
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id",id);
        extraClaims.put("nickname",nickname);
        extraClaims.put("role",role);
        return extraClaims;
    }


}
