package com.newnomal.booth_reservation.config;

import com.newnomal.booth_reservation.common.Role;
import com.newnomal.booth_reservation.common.TokenInfo;
import com.newnomal.booth_reservation.common.TokenStatus;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    //Develop 전용 하드코딩
    final private String secret = "TESTTESTTESTESTESTESTESTESTESTESTESTESTESTESTES1234!@#!@#";

    //Develop용 AccessToken 유효 시간(5분) 1초 * 60 * 5
    private final Long AccessTokenExpirationTime = 1000L * 60 * 5;
    //Develop용 RefreshToken 유효 시간(15분) 1초 * 60 * 15
    private final Long RefreshTokenExpirationTime = 1000L * 60 * 15;


    //Access Token 생성
    //extraClaims 객체에 들어있는 데이터(userID, userNickname, userRole)
    public String generateAccessToken(
            Map<String, Object> extraClaims
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + AccessTokenExpirationTime))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }

    //Refresh Token 생성
    //extraClaims 객체에 들어있는 데이터(userID, userNickname, userRole)
    public String generateRefreshToken(
            Map<String, Object> extraClaims
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + RefreshTokenExpirationTime))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }

    //토큰에서 정보 추출 이후 TokenInfo 객체 만들어서 반환
    public TokenInfo extractUser(String token){
        Claims claims = extractAllClaims(token);
        return TokenInfo.builder()
                .id(claims.get("id", Long.class))
                .nickname(claims.get("username", String.class))
                .role(Role.valueOf(claims.get("role", String.class)))
                .build();
    }

    //토큰 검증(1.유효성, 2.만료, 3.유효하지 않음 검증)
    public TokenStatus checkToken(String token) {
        try {
            //유효성 검증
            Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .parseClaimsJws(token);
            return TokenStatus.VALID;
        }catch (ExpiredJwtException e){
            return TokenStatus.EXPIRED;
        }catch (Exception e){
            return TokenStatus.INVALID;
        }
    }

    //역할 검증(1.토큰이 유효하다면 역할 리턴, 2.토큰 만료, 3.토큰 유효하지 않음)
    public TokenStatus checkRole(String token) {
        Claims claims;
        try {
            //유효성 검증
            claims = Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            //역할 검증
            if (claims.get("role").equals(Role.ADMIN)){
                return TokenStatus.VALID;
            }else {
                return TokenStatus.NOT_AUTHORIZED;
            }
        }catch (ExpiredJwtException e){
            return TokenStatus.EXPIRED;
        }catch (JwtException | IllegalArgumentException e){
            return TokenStatus.INVALID;
        }
    }

    //토큰에서 Claims 객체 추출
    private Claims extractAllClaims(String token) {
        return (Claims) Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parse(token)
                .getBody();
    }



}