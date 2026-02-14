package com.aprimorar.api.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ParentRequestDto(
        @NotNull(message = "Parent name can't be null")
        String name,

        @NotNull(message = "Parent email can't be null")
        @Email
        String email,

        @NotNull(message = "Parent contact can't be null")
        String contact,

        @NotNull(message = "Parent cpf can't be null")
        String cpf
) {
}
