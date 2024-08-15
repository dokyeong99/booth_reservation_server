package com.newnomal.booth_reservation.controller;


import com.newnomal.booth_reservation.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authority")
public class AuthorityController {
    private final AuthorityService authorityService;

}
