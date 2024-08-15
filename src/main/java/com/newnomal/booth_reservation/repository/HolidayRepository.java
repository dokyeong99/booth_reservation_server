package com.newnomal.booth_reservation.repository;

import com.newnomal.booth_reservation.domain.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
}
