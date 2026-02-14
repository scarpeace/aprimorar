package com.aprimorar.api.controller.dto;

import com.aprimorar.api.enums.Activity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.util.Date;

public record StudentRequestDto(
        @NotNull(message = "Student name can't be null")
        String name,

        @NotNull(message = "Student birthdate can't be null")
        @Past(message = "Student birthdate should be in the past")
        Date birthdate,

        @NotNull(message = "Student CPF can't be null")
        String cpf,

        @NotBlank(message = "Student school can't be blank")
        String school,

        @NotBlank(message = "Student contact number can't be blank")
        String contact,

        @NotBlank(message = "Student email can't be blank")
        @Email
        String email,

        @NotNull(message = "Student activity can't be null")
        Activity activity,

        @NotNull(message = "Student Address can't be null")
        AddressRequestDto address,

        @NotNull(message = "Student Parent can't be null")
        ParentRequestDto parent
) {
}
