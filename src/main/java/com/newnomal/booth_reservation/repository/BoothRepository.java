package com.newnomal.booth_reservation.repository;

import com.newnomal.booth_reservation.domain.entity.Booth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoothRepository extends JpaRepository<Booth,Long> {
    List<Booth> findByAuthorityId(Long authorityId);

    @Query("select b from Booth  b " +
            "where b.state = com.newnomal.booth_reservation.domain.state.BoothState.VALID")
    List<Booth> findByAuthorityIdStateVAlid(Long authorityId);

    @Query("select size(b) from Booth b " +
            "where b.authorityId = :authorityId and " +
            "b.state = com.newnomal.booth_reservation.domain.state.BoothState.VALID")
    Integer getValidBoothLength(Long authorityId);

}
