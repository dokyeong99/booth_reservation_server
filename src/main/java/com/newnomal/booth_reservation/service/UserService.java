package com.newnomal.booth_reservation.service;

import com.newnomal.booth_reservation.common.RestResult;
import com.newnomal.booth_reservation.common.TokenInfo;
import com.newnomal.booth_reservation.config.JwtService;
import com.newnomal.booth_reservation.domain.entity.User;
import com.newnomal.booth_reservation.domain.request.UserRequest;
import com.newnomal.booth_reservation.domain.response.UserResponse;
import com.newnomal.booth_reservation.domain.state.UserState;
import com.newnomal.booth_reservation.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    //token으로 유저 정보 반환
    public ResponseEntity<RestResult<Object>> getMe(String token) {
        //유저 정보 받기
        TokenInfo tokenInfo = jwtService.extractUser(token);
        User user = userRepository.getReferenceById(tokenInfo.getId());

        return ResponseEntity.ok(new RestResult<>("SUCCESS",new UserResponse(user)));
    }


    //유저 정보 업데이트
    public ResponseEntity<RestResult<Object>> updateUser(UserRequest userRequest,
                                                         String token) {
        //유저 정보 받기
        TokenInfo tokenInfo = jwtService.extractUser(token);
        User user = userRepository.getReferenceById(tokenInfo.getId());

        //유저 정보 업데이트 진행
        user.setNickname(userRequest.getNickname());
        user.setProfileImage(userRequest.getProfileImage());
        user.setSecretPassword(userRequest.getSecretPassword());
        userRepository.save(user);
        return ResponseEntity.ok(new RestResult<>("SUCCESS",new UserResponse(user)));

    }

    //유저 삭제
    public ResponseEntity<RestResult<Object>> deleteUser(String token) {
        //유저 정보 받기
        TokenInfo tokenInfo = jwtService.extractUser(token);
        User user = userRepository.getReferenceById(tokenInfo.getId());

        //유저 상태 삭제 처리
        user.setState(UserState.DELETED);
        return ResponseEntity.ok(new RestResult<>("SUCCESS","유저 삭제처리 완료"));

    }
}
