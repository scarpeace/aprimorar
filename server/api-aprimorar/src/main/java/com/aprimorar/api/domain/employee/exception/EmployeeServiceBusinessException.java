package com.aprimorar.api.domain.employee.exception;

public class EmployeeServiceBusinessException extends RuntimeException {
    public EmployeeServiceBusinessException(String message) {
        super(message);
    }
}

