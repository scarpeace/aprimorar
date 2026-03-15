package com.aprimorar.api.domain.parent.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ParentRequestDTO(
        @NotNull(message = "Nome do responsável é obrigatório")
        String name,

        @NotNull(message = "Email do responsável é obrigatório")
        @Email(message = "Email deve ser um endereço de email válido")
        String email,

        @NotNull(message = "Contato do responsável é obrigatório")
        String contact,

        @NotNull(message = "CPF do responsável é obrigatório")
        String cpf
) {
}
