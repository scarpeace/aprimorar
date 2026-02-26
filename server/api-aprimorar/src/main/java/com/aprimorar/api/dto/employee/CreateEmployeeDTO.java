package com.aprimorar.api.dto.employee;

import com.aprimorar.api.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record CreateEmployeeDTO (

        @NotBlank(message = "Employee name can't be blank")
        String name,

        @NotNull(message = "Employee birthdate can't be null")
        @Past(message = "Employee birthdate should be in the past")
        LocalDate birthdate,

        @NotBlank(message = "Employee pix can't be blank")
        String pix,

        @NotBlank(message = "Employee contact can't be blank")
        @Pattern(regexp = "^\\(\\d{2}\\)\\d{5}-\\d{4}$", message = "Contact must be in format (XX)XXXXX-XXXX")
        String contact,

        @NotBlank(message = "Employee CPF can't be blank")
        @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "CPF must be in format XXX.XXX.XXX-XX")
        String cpf,

        @NotBlank(message = "Employee email can't be blank")
        @Email(message = "Email must be a valid email address")
        String email,

        @NotNull(message = "Employee Role can't be null")
        Role role) {
}
