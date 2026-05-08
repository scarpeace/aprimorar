package com.aprimorar.api.domain.event.api.exception;

public class NotAllowedToUpdateEventException extends RuntimeException {
    public NotAllowedToUpdateEventException(String message) {
        super(message);
    }
}
