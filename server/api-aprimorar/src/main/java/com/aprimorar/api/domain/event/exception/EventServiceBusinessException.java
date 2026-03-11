package com.aprimorar.api.domain.event.exception;

public class EventServiceBusinessException extends RuntimeException {
    public EventServiceBusinessException(String message) {
        super(message);
    }
}
