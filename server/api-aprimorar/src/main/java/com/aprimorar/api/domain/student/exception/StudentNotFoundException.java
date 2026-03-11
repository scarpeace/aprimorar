package com.aprimorar.api.domain.student.exception;

import java.util.UUID;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(UUID studentId) {
        super("Estudante com o ID informado não encontrado no banco de dados " + studentId);
    }
}
