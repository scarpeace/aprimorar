package com.aprimorar.api.domain.parent.dto;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados do responsável retornados pela API")
public record ParentResponseDTO(
        @NotNull
        @Schema(description = "ID do responsável", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID parentId,

        @NotNull
        @Schema(description = "Nome do responsável", example = "João Silva")
        String name,

        @NotNull
        @Schema(description = "Email do responsável", example = "email@email.com")
        String email,

        @NotNull
        @Schema(description = "Contato do responsável", example = "11999999999")
        String contact,

        @NotNull
        @Schema(description = "CPF do responsável", example = "12345678901")
        String cpf,

        @Schema(nullable = true, example = "2023-01-01T00:00:00Z", description = "Data e hora quando o aluno foi arquivado")
        Instant archivedAt,

        @NotNull
        @Schema(description = "Data e hora quando o aluno foi criado",example = "2023-01-01T00:00:00Z")
        Instant createdAt,

        @Schema(nullable = true,description = "Data e hora quando o aluno foi atualizado", example = "2023-01-01T00:00:00Z")
        Instant updatedAt
        ) {

}
