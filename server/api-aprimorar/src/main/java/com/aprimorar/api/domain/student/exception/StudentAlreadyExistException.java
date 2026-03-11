package com.aprimorar.api.domain.student.exception;

public class StudentAlreadyExistException extends RuntimeException {
    public StudentAlreadyExistException() {
        super("Aluno já cadastrado no banco de dados");
    }
}
