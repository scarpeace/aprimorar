package com.aprimorar.api.domain.parent.dto;

import com.aprimorar.api.validation.ValidationPatterns;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateParentDTO(
        @NotNull(message = "Nome do responsável é obrigatório")
        String name,

        @NotNull(message = "Email do responsável é obrigatório")
        @Email(message = "Email deve ser um endereço de email válido")
        String email,

        @NotNull(message = "Contato do responsável é obrigatório")
        @Pattern(regexp = ValidationPatterns.PHONE_BR, message = ValidationPatterns.PHONE_BR_MESSAGE)
        String contact,

        @NotNull(message = "CPF do responsável é obrigatório")
        @Pattern(regexp = ValidationPatterns.CPF, message = ValidationPatterns.CPF_MESSAGE)
        String cpf
) {
}
