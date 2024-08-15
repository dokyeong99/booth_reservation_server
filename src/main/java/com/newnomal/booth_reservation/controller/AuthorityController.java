package com.newnomal.booth_reservation.controller;

import com.newnomal.booth_reservation.common.RestResult;
import com.newnomal.booth_reservation.config.AdminCheck;
import com.newnomal.booth_reservation.config.TokenRequired;
import com.newnomal.booth_reservation.domain.request.AuthorityRequest;
import com.newnomal.booth_reservation.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authority")
public class AuthorityController {
    private final AuthorityService authorityService;


    //기관 삽입
    @AdminCheck
    @PostMapping
    public ResponseEntity<RestResult<Object>> createAuthority(@RequestBody AuthorityRequest authorityRequest) {
        return authorityService.createAuthority(authorityRequest);
    }


    //기관 정보 불러오기
    @TokenRequired
    @GetMapping("/{id}")
    public ResponseEntity<RestResult<Object>> getAuthority(@PathVariable Long id) {
        return authorityService.getAuthority(id);
    }

    //State가 valid한 기관 전부 불러오기
    //Authority가 많지 않다고 가정하기 때문에 페이징 필요 없음
    @GetMapping("/getAllStateValid")
    public ResponseEntity<RestResult<Object>> getAllStateValid() {
        return authorityService.getAllStateValid();
    }

    //기관 정보 업데이트(어드민만 가능)
    @AdminCheck
    @PutMapping("/{id}")
    public ResponseEntity<RestResult<Object>> updateAuthority(@PathVariable Long id, @RequestBody AuthorityRequest authorityRequest) {
        return authorityService.updateAuthority(id, authorityRequest);
    }


    //기관 삭제처리(완전 삭제가 아닌 state를 삭제 상태로 변환)
    @AdminCheck
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResult<Object>> deleteAuthority(@PathVariable Long id) {
        return authorityService.deleteAuthority(id);
    }
}