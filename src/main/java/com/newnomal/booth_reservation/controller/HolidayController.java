package com.newnomal.booth_reservation.controller;

import com.newnomal.booth_reservation.common.RestResult;
import com.newnomal.booth_reservation.config.AdminCheck;
import com.newnomal.booth_reservation.config.TokenRequired;
import com.newnomal.booth_reservation.domain.request.HolidayRequest;
import com.newnomal.booth_reservation.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/holiday")
public class HolidayController {
    private final HolidayService holidayService;

    @AdminCheck
    @PostMapping
    public ResponseEntity<RestResult<Object>> createHoliday(@RequestBody HolidayRequest holidayRequest) {
        return holidayService.createHoliday(holidayRequest);
    }

    @TokenRequired
    @GetMapping("/{id}")
    public ResponseEntity<RestResult<Object>> getHoliday(@PathVariable Long id) {
        return holidayService.getHoliday(id);
    }

    //특정 기관이 휴일인지 확인
    @TokenRequired
    @GetMapping("/authority/{authorityId}")
    public ResponseEntity<RestResult<Object>> getHolidaysByAuthority(@PathVariable Long authorityId) {
        return holidayService.getHolidaysByAuthority(authorityId);
    }


    @AdminCheck
    @PutMapping("/{id}")
    public ResponseEntity<RestResult<Object>> updateHoliday(@PathVariable Long id, @RequestBody HolidayRequest holidayRequest) {
        return holidayService.updateHoliday(id, holidayRequest);
    }

    @AdminCheck
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResult<Object>> deleteHoliday(@PathVariable Long id) {
        return holidayService.deleteHoliday(id);
    }
}