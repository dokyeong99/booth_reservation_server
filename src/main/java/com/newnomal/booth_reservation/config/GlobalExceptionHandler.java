package com.newnomal.booth_reservation.config;


import com.newnomal.booth_reservation.common.RestError;
import com.newnomal.booth_reservation.common.RestResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



import static org.springframework.web.servlet.function.ServerResponse.status;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<RestResult<Object>> handleTokenException(TokenException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new RestResult<>("CONFLICT",new RestResult<>("TOKEN_CONFLICT",e.getMessage())));
    }
}
