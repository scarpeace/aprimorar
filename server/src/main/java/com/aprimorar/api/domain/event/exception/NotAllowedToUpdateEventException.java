package com.aprimorar.api.domain.event.exception;

public class NotAllowedToUpdateEventException extends RuntimeException {
    public NotAllowedToUpdateEventException(String message) {
        super(message);
    }
}
