package com.newnomal.booth_reservation.controller;


import com.newnomal.booth_reservation.common.RestResult;
import com.newnomal.booth_reservation.config.TokenRequired;
import com.newnomal.booth_reservation.domain.entity.User;
import com.newnomal.booth_reservation.domain.request.UserRequest;
import com.newnomal.booth_reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;


    //토큰으로 유저 정보 반환
    @TokenRequired
    @PostMapping("/getMe")
    public ResponseEntity<RestResult<Object>> getMe(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        return userService.getMe(token);
    }


    //유저 정보 업데이트(이미지, 패스워드, 닉네임 변경 가능)
    @TokenRequired
    @PutMapping("/updateUser")
    public ResponseEntity<RestResult<Object>> updateUser(@RequestBody UserRequest userRequest,
                                                         @RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        return userService.updateUser(userRequest, token);
    }


    //유저 삭제 처리(완전 삭제 처리하지 않고 state변경으로 삭제 상태 처리)
    @TokenRequired
    @DeleteMapping
    public ResponseEntity<RestResult<Object>> deleteUser(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        return userService.deleteUser(token);
    }

}
