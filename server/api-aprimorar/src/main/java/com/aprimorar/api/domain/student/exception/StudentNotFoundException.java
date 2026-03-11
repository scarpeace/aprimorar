package com.aprimorar.api.domain.student.exception;


public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException() {
        super("Estudante com o ID informado não encontrado no banco de dados ");
    }
}
