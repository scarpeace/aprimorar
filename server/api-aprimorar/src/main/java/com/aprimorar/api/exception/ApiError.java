package com.aprimorar.api.exception;

import lombok.Getter;

import java.time.Instant;

@Getter
public class ApiError {

    private final int status;
    private final String error;
    private final String code;
    private final String message;
    private final String path;
    private final Instant timestamp;

    public ApiError(int status, String error, String code, String message, String path, Instant timestamp) {
        this.status = status;
        this.error = error;
        this.code = code;
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
    }
}
