package com.aprimorar.api.domain.employee;

import com.aprimorar.api.domain.student.exception.InvalidStudentException;

public class EmployeeRules {

	public static void validate(Employee employee) {
        validateRequiredFields(employee);
    }

    private static void validateRequiredFields(Employee employee) {
        if (employee.getName() == null || employee.getName().isBlank()) {
            throw new InvalidStudentException("Nome do colaborador não pode estar vazio");
        }
        if (employee.getBirthdate() == null) {
            throw new InvalidStudentException("A data de nascimento do colaborador não pode estar vazia");
        }
        if (employee.getCpf() == null || employee.getCpf().isBlank()) {
            throw new InvalidStudentException("CPF do colaborador não pode estar vazio");
        }

        if (employee.getContact() == null || employee.getContact().isBlank()) {
            throw new InvalidStudentException("Contato do colaborador não pode estar vazio");
        }
        if (employee.getEmail() == null || employee.getEmail().isBlank()) {
            throw new InvalidStudentException("Email do colaborador não pode estar vazio");
        }
    }
}
