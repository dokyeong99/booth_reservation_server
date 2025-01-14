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


    //특정 유저가 특정 기관에서 예약하고자 할 때 해당 기관에서의 남아있는 예약 가능 time zone 반환
    //예로 해당 예약 기관의 최대 이용 가능 timezone이 10일 때 6을 이미 예약했다면 4반환
    @TokenRequired
    @GetMapping("/availability")
    public ResponseEntity<RestResult<Object>> getBoothAvailability(
            @RequestParam Long authorityId,
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return boothService.getBoothAvailability(authorityId, userId, date);
    }

    //단일 부스에서 예약 불가능한 타임 zone 반환
    @GetMapping("/boothReservedTimeZone")
    public ResponseEntity<RestResult<Object>> getReservedTimeZone(
            @RequestParam Long boothId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return boothService.getReservedTimeZone(boothId, date);
    }



}


