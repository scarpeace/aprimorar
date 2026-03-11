package com.aprimorar.api.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@Setter
public class ApiError {

    private final String message;
    private final HttpStatus httpStatus;
    private final Instant timestamp;

    public ApiError(String message, HttpStatus httpStatus, Instant timestamp) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }
}