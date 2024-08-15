package com.newnomal.booth_reservation.controller;

import com.newnomal.booth_reservation.common.RestResult;
import com.newnomal.booth_reservation.config.AdminCheck;
import com.newnomal.booth_reservation.config.TokenRequired;
import com.newnomal.booth_reservation.domain.request.BoothRequest;
import com.newnomal.booth_reservation.service.BoothService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/booth")
public class BoothController {
    private final BoothService boothService;

    //부스 생성
    @AdminCheck
    @PostMapping
    public ResponseEntity<RestResult<Object>> createBooth(@RequestBody BoothRequest boothRequest) {
        return boothService.createBooth(boothRequest);
    }


    @TokenRequired
    @GetMapping("/{id}")
    public ResponseEntity<RestResult<Object>> getBooth(@PathVariable Long id) {
        return boothService.getBooth(id);
    }


    //특정 기관 부스 정보 반환(부스가 많지 않기 때문에 페이징 불필요)
    //State가 valid한 경우에만 반환
    @TokenRequired
    @GetMapping("/authority/{authorityId}")
    public ResponseEntity<RestResult<Object>> getBoothsByAuthority(@PathVariable Long authorityId) {
        return boothService.getBoothsByAuthority(authorityId);
    }


    //부스 수정(어드민만 가능)
    @AdminCheck
    @PutMapping("/{id}")
    public ResponseEntity<RestResult<Object>> updateBooth(@PathVariable Long id, @RequestBody BoothRequest boothRequest) {
        return boothService.updateBooth(id, boothRequest);
    }


    //부스 삭제(어드민만 가능)
    //완전 삭제가 아닌 state만 변경
    @AdminCheck
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResult<Object>> deleteBooth(@PathVariable Long id) {
        return boothService.deleteBooth(id);
    }


    //특정 유저가 특정 기관에서 예약하고자 할 때 해당 기관에서의 남아있는 예약 가능 time zone 개수 반환
    //해당 기관이 10의 예약이 가능하고, 이미 유저가 2의 예약을 완료 했다면 8을 반환
    @TokenRequired
    @GetMapping("/availability")
    public ResponseEntity<RestResult<Object>> getBoothAvailability(
            @RequestParam Long authorityId,
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return boothService.getBoothAvailability(authorityId, userId, date);
    }

    @TokenRequired
    @GetMapping("/reservedTimeZone")
    public ResponseEntity<RestResult<Object>> getReservedTimeZone(
            @RequestParam Long boothId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return boothService.getReservedTimeZone(boothId, date);
    }


}