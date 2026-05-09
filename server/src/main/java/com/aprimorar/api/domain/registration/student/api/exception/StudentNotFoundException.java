package com.aprimorar.api.domain.registration.student.api.exception;


public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);
    }
}
