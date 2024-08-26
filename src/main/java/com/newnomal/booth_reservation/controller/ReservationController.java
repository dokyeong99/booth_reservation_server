package com.newnomal.booth_reservation.controller;

import com.newnomal.booth_reservation.common.RestResult;
import com.newnomal.booth_reservation.config.TokenRequired;
import com.newnomal.booth_reservation.domain.request.ReservationRequest;
import com.newnomal.booth_reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    @TokenRequired
    @PostMapping
    public ResponseEntity<RestResult<Object>> createReservation(@RequestBody ReservationRequest reservationRequest) {
        return reservationService.createReservation(reservationRequest);
    }

    @TokenRequired
    @GetMapping("/{id}")
    public ResponseEntity<RestResult<Object>> getReservation(@PathVariable Long id) {
        return reservationService.getReservation(id);
    }

    @TokenRequired
    @GetMapping("/user/{userId}")
    public ResponseEntity<RestResult<Object>> getUserReservations(@PathVariable Long userId) {
        return reservationService.getUserReservations(userId);
    }


    @TokenRequired
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResult<Object>> cancelReservation(@PathVariable Long id) {
        return reservationService.cancelReservation(id);
    }

    @TokenRequired
    @GetMapping("/verifyQRCode/{qrIdentifier}/{date}/{timeZone}")
    public ResponseEntity<RestResult<Object>> verifyQrCode(@RequestHeader("Authorization") String authorization,
                                                           @PathVariable String qrIdentifier,
                                                           @PathVariable LocalDate date,
                                                           @PathVariable Integer timeZone) {
        String token = authorization.substring(7);
        return reservationService.verifyQRCode(token, qrIdentifier, date, timeZone);
    }
}