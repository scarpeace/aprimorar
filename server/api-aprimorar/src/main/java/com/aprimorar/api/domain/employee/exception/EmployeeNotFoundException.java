package com.aprimorar.api.domain.employee.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException() {
        super("O colaborador com o ID informado não foi encontrado no banco de dados ");
    }
}

