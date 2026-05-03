package com.aprimorar.api.domain.parent.exception;

public class ParentAlreadyExistsException extends RuntimeException {
    public ParentAlreadyExistsException(String message) {
        super(message);
    }
}
