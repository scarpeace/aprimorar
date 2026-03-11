package com.aprimorar.api.domain.event.exception;

public class EventWithArchivedStudentException extends RuntimeException {
    public EventWithArchivedStudentException(String message){
        super(message);
    }
}

