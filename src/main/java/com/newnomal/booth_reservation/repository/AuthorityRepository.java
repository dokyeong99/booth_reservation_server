package com.newnomal.booth_reservation.repository;

import com.newnomal.booth_reservation.domain.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    @Query("select a from Authority a " +
            "where a.status == com.newnomal.booth_reservation.domain.state.AuthorityState.VALID")
    List<Authority> getAllStateValid();
}
