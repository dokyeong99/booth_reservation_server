package com.newnomal.booth_reservation.config;


import com.newnomal.booth_reservation.common.Role;
import com.newnomal.booth_reservation.common.TokenStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
@RequiredArgsConstructor
public class SecretAspect { // 토큰 확인, 유저 확인, 어드민 확인을 위한 AOP 집합
    private final JwtService jwtService;


    //토큰 상태 확인 AOP
    //헤더를 확인하여 Authoriztion에서 토큰을 확인하며, 토큰이 만료되었거나 유효하지 않은 경우 Exception 반환, 유효하다면 무반응
    @Before("@annotation(tokenRequired)")
    public void tokenRequired(TokenRequired tokenRequired) {
        //Authorization 정보를 가져오기 위하여 Servlet Request 객체 속의 Attribute 가져옴
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //Attribute가 접근 불가하다면 에러 리턴
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            //헤더에서 토큰 가져오기
            String token = request.getHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // "Bearer " prefix 제거
                TokenStatus result = jwtService.checkToken(token);//토큰 유효성 검정(유효성, 만료 검증)
                if (result.equals(TokenStatus.INVALID)) {
                    throw new TokenException("토큰이 유효하지 않습니다.");
                } else if (result.equals(TokenStatus.EXPIRED)) {
                    throw new TokenException("토큰이 만료되었습니다.");
                }
            } else {
                throw new TokenException("Authoriztion 헤더에 Bearer을 포함한 유효한 토큰을 넣어 주십시오.");
            }
        } else {
            throw new TokenException("No request attributes available");
        }
    }



    //어드민 권한 인증 AOP
    @Before("@annotation(adminCheck)")
    public void AdminCehck(AdminCheck adminCheck) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //Attribute가 접근 불가하다면 에러 리턴
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            //헤더에서 토큰 가져오기
            String token = request.getHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // "Bearer " prefix 제거
                TokenStatus result = jwtService.checkRole(token);//토큰 유효성 검정(유효성, 만료 검증)
                if (!result.equals(TokenStatus.NOT_AUTHORIZED)) {//어드민 확인
                    throw new TokenException("관리자 권한이 없습니다.");
                } else if (result.equals(TokenStatus.INVALID)) {
                    throw new TokenException("토큰이 유효하지 않습니다.");
                } else if (result.equals(TokenStatus.EXPIRED)) {
                    throw new TokenException("토큰이 만료되었습니다.");
                }
            } else {
                throw new TokenException("Authoriztion 헤더에 Bearer을 포함한 유효한 토큰을 넣어 주십시오.");
            }
        } else {
            throw new TokenException("No request attributes available");
        }
    }

}
