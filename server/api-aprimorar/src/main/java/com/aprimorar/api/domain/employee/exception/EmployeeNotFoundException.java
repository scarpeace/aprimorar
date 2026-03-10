package com.aprimorar.api.domain.employee.exception;

import java.util.UUID;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(UUID id) {
        super("O colaborador com o ID informado não foi encontrado nos registros do banco de dados " + id);
    }
}

