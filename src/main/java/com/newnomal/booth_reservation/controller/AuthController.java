package com.newnomal.booth_reservation.controller;

import com.newnomal.booth_reservation.common.RestResult;
import com.newnomal.booth_reservation.config.TokenRequired;
import com.newnomal.booth_reservation.domain.request.UserAuthRequest;
import com.newnomal.booth_reservation.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    //1.Oauth 회원가입 2.Oauth 로그인 3.Token 로그인 4.Refresh Token 으로 Access 토큰 재발급

    private final AuthService authService;

    //Oauth 회원가입
    //kakaoID를 기반으로 회원가입 진행 이후 토큰 반환
    //이미 가입한 아이디면 error 반환
    @PostMapping("/user/signUp")
    public ResponseEntity<RestResult<Object>> signUp(@RequestBody UserAuthRequest request) {
        return authService.signUp(request);
    }


    //Oauth 로그인(토큰이 만료되어 Refresh 토큰도 재발급 받는 상황)
    //kakaoUUID 기반 유저 인증 및 토큰 반환
    //없는 유저면 에러 반환
    @PostMapping("/oauthLogin")
    public ResponseEntity<RestResult<Object>> oauthLogin(@RequestBody UserAuthRequest request) {
        return authService.oauthLogin(request);
    }


    //Token 로그인(토큰이 만료되지 않아 토큰으로 로그인이 가능한 상황)
    //Token이 유효하면 토큰에 저장된 ID값을 기준으로 OK Status반환, 아닐 경우 Error 반환
    //AOP로 해결
    @GetMapping("/tokenLogin")
    @TokenRequired
    public ResponseEntity<RestResult<Object>> tokenLogin() {
        return ResponseEntity.ok(new RestResult<>("SUCCESS","로그인 완료"));
    }

    //RefreshToken으로 AccessToken 재발급
    //Front에서 Authorization 헤더에 RefreshToken을 넣어서 제공함
    @GetMapping("/refreshExpiredToken")
    @TokenRequired
    public ResponseEntity<RestResult<Object>> refreshExpiredToken(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        return authService.refreshExpiredToken(token);
    }


}
