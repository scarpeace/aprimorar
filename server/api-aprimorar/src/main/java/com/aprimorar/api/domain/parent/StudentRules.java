package com.aprimorar.api.domain.parent;

import com.aprimorar.api.domain.student.Student;
import com.aprimorar.api.domain.student.exception.InvalidStudentException;

public class StudentRules {

    public static void validate(Student student){
        validateRequiredFields(student);
    }

    private static void validateRequiredFields(Student student) {
        if (student.getName() == null || student.getName().isBlank()) {
            throw new InvalidStudentException("Nome do estudante não pode estar vazio");
        }
        if (student.getBirthdate() == null) {
            throw new InvalidStudentException("A data de nascimento não pode estar vazio");
        }
        if (student.getCpf() == null || student.getCpf().isBlank()) {
            throw new InvalidStudentException("CPF do estudante não pode estar vazio");
        }
        if (student.getSchool() == null || student.getSchool().isBlank()) {
            throw new InvalidStudentException("Escola do estudante não pode estar vazio");
        }
        if (student.getContact() == null || student.getContact().isBlank()) {
            throw new InvalidStudentException("Contato do estudante não pode estar vazio");
        }
        if (student.getEmail() == null || student.getEmail().isBlank()) {
            throw new InvalidStudentException("Email do estudante não pode estar vazio");
        }
        if (student.getParent() == null) {
            throw new InvalidStudentException("O ID do responsável não pode ser nulo");
        }
        if (student.getAddress() == null) {
            throw new InvalidStudentException("O endereço do aluno não pode ser nulo");
        }
    }
}
