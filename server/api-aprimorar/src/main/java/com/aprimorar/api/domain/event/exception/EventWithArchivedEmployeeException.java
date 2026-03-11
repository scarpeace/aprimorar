package com.aprimorar.api.domain.event.exception;

public class EventWithArchivedEmployeeException extends RuntimeException {
    public EventWithArchivedEmployeeException(String message) {
        super(message);
    }
}

