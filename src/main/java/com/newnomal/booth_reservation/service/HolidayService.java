package com.newnomal.booth_reservation.service;

import com.newnomal.booth_reservation.repository.HolidayRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class HolidayService {
    private final HolidayRepository holidayRepository;

}
