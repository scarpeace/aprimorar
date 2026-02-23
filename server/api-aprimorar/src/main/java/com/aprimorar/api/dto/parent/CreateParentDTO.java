package com.aprimorar.api.dto.parent;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateParentDTO(
        @NotNull(message = "Parent name can't be null")
        String name,

        @NotNull(message = "Parent email can't be null")
        @Email
        String email,

        @NotNull(message = "Parent contact can't be null")
        @Pattern(regexp = "^\\(\\d{2}\\)\\d{4,5}-\\d{4}$", message = "Contact must be in format (XX)XXXXX-XXXX")
        String contact,

        @NotNull(message = "Parent cpf can't be null")
        @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "CPF must be in format XXX.XXX.XXX-XX")
        String cpf
) {
}
