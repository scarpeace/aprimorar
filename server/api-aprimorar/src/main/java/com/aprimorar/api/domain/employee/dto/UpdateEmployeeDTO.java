package com.aprimorar.api.domain.employee.dto;

import com.aprimorar.api.enums.Role;
import com.aprimorar.api.validation.ValidationPatterns;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UpdateEmployeeDTO(
        String name,

        @Past(message = "A data de nascimento do funcionário deve estar no passado")
        LocalDate birthdate,

        String pix,

        @Pattern(regexp = ValidationPatterns.PHONE_BR, message = ValidationPatterns.PHONE_BR_MESSAGE)
        String contact,

        @Pattern(regexp = ValidationPatterns.CPF, message = ValidationPatterns.CPF_MESSAGE)
        String cpf,

        @Email(message = "Email deve ser um endereço de email válido")
        String email,

        Role role
) {
}
