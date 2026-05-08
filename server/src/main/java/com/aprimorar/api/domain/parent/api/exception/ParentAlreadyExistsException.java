package com.aprimorar.api.domain.parent.api.exception;

public class ParentAlreadyExistsException extends RuntimeException {
    public ParentAlreadyExistsException(String message) {
        super(message);
    }
}
