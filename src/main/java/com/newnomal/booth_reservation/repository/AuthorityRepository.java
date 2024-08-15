package com.newnomal.booth_reservation.repository;

import com.newnomal.booth_reservation.domain.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
