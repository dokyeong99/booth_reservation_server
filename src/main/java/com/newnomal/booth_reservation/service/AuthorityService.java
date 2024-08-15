package com.newnomal.booth_reservation.service;

import com.newnomal.booth_reservation.repository.AuthorityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

}

