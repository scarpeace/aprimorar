package com.aprimorar.api.domain.parent.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Formato de payload para criar um novo responsável")
public record ParentRequestDTO(
        @NotBlank(message = "Nome do responsável é obrigatório")
        @Schema(nullable = false, description = "Nome do responsável", example = "João Silva")
        String name,

        @NotBlank(message = "Email do responsável é obrigatório")
        @Email(message = "Email deve ser um endereço de email válido")
        @Schema(nullable = false, description = "Email do responsável", example = "[EMAIL_ADDRESS]")
        String email,

        @NotBlank(message = "Contato do responsável é obrigatório")
        @Schema(nullable = false, description = "Contato do responsável", example = "11999999999")
        String contact,

        @NotBlank(message = "CPF do responsável é obrigatório")
        @Schema(nullable = false, description = "CPF do responsável", example = "12345678901")
        String cpf
) {
}
