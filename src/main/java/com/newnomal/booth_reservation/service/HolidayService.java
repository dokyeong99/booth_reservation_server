package com.newnomal.booth_reservation.service;

import com.newnomal.booth_reservation.common.RestResult;
import com.newnomal.booth_reservation.domain.entity.Authority;
import com.newnomal.booth_reservation.domain.entity.Holiday;
import com.newnomal.booth_reservation.domain.request.HolidayRequest;
import com.newnomal.booth_reservation.domain.response.HolidayResponse;
import com.newnomal.booth_reservation.repository.AuthorityRepository;
import com.newnomal.booth_reservation.repository.HolidayRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class HolidayService {
    private final HolidayRepository holidayRepository;
    private final AuthorityRepository authorityRepository;

    public ResponseEntity<RestResult<Object>> createHoliday(HolidayRequest holidayRequest) {
        Authority authority = authorityRepository.findById(holidayRequest.getAuthorityId())
                .orElseThrow(() -> new RuntimeException("Authority not found"));

        Holiday holiday = new Holiday();
        holiday.setDate(holidayRequest.getDate());
        holiday = holidayRepository.save(holiday);

        authority.getHolidayList().add(holiday);
        authorityRepository.save(authority);

        return ResponseEntity.ok(new RestResult<>("SUCCESS", new HolidayResponse(holiday)));
    }

    public ResponseEntity<RestResult<Object>> getHoliday(Long id) {
        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Holiday not found"));
        return ResponseEntity.ok(new RestResult<>("SUCCESS", new HolidayResponse(holiday)));
    }

    public ResponseEntity<RestResult<Object>> getHolidaysByAuthority(Long authorityId) {
        Authority authority = authorityRepository.findById(authorityId)
                .orElseThrow(() -> new RuntimeException("Authority not found"));
        List<HolidayResponse> holidays = authority.getHolidayList().stream()
                .map(HolidayResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new RestResult<>("SUCCESS", holidays));
    }

    public ResponseEntity<RestResult<Object>> updateHoliday(Long id, HolidayRequest holidayRequest) {
        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Holiday not found"));
        holiday.setDate(holidayRequest.getDate());
        holiday = holidayRepository.save(holiday);
        return ResponseEntity.ok(new RestResult<>("SUCCESS", new HolidayResponse(holiday)));
    }

    public ResponseEntity<RestResult<Object>> deleteHoliday(Long id) {
        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Holiday not found"));
        holidayRepository.delete(holiday);
        return ResponseEntity.ok(new RestResult<>("SUCCESS", "Holiday deleted successfully"));
    }
}