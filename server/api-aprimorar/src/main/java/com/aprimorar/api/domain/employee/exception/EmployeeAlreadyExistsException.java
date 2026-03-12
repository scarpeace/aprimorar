package com.aprimorar.api.domain.employee.exception;

public class EmployeeAlreadyExistsException extends RuntimeException {
    public EmployeeAlreadyExistsException() {
        super("O colaborador com o ID informado já existe no banco de dados ");
    }
}

