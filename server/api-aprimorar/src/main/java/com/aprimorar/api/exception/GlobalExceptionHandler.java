package com.aprimorar.api.exception;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Clock applicationClock;

    public GlobalExceptionHandler(Clock applicationClock) {
        this.applicationClock = applicationClock;
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
                ex.getMessage(),
                HttpStatus.CONFLICT,
                applicationClock.instant());

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

}
