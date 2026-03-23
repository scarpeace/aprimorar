package com.aprimorar.api.domain.parent.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ParentRequestDTO(
        @NotNull(message = "Nome do responsável é obrigatório")
        @Schema(description = "Nome do responsável", example = "João Silva")
        String name,

        @NotNull(message = "Email do responsável é obrigatório")
        @Email(message = "Email deve ser um endereço de email válido")
        @Schema(description = "Email do responsável", example = "[EMAIL_ADDRESS]")
        String email,

        @NotNull(message = "Contato do responsável é obrigatório")
        @Schema(description = "Contato do responsável", example = "11999999999")
        String contact,

        @NotNull(message = "CPF do responsável é obrigatório")
        @Schema(description = "CPF do responsável", example = "12345678901")
        String cpf
) {
}
