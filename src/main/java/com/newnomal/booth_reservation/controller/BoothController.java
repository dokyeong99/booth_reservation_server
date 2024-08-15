package com.newnomal.booth_reservation.controller;


import com.newnomal.booth_reservation.service.BoothService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/booth")
public class BoothController {
    private final BoothService boothService;

}


