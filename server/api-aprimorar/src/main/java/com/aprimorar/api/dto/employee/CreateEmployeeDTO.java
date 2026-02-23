package com.aprimorar.api.dto.employee;

import com.aprimorar.api.enums.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record CreateEmployeeDTO (

        @NotNull(message = "Employee name can't be null")
        String name,

        @NotNull(message = "Employee birthdate can't be null")
        @Past(message = "Employee birthdate should be in the past")
        LocalDate birthdate,

        @NotNull(message = "Employee pix can't be null")
        String pix,

        @NotNull(message = "Employee concat can't be null")
        String contact,

        @NotNull(message = "Employee CPF can't be null")
        String cpf,

        @NotNull(message = "Employee Role can't be null")
        Role role) {
}
