package com.aprimorar.api.domain.parent.dto;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados do aluno retornados pela API")
public record ParentResponseDTO(
        @Schema(description = "ID do responsável", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull
        UUID parentId,

        @Schema(description = "Nome do responsável", example = "João Silva")
        @NotNull
        String name,

        @Schema(description = "Email do responsável", example = "email@email.com")
        @NotNull
        String email,

        @Schema(description = "Contato do responsável", example = "11999999999")
        @NotNull
        String contact,

        @Schema(description = "CPF do responsável", example = "12345678901")
        @NotNull
        String cpf,

        @Schema(nullable = true, example = "2023-01-01T00:00:00Z", description = "Data e hora quando o aluno foi arquivado")
        Instant archivedAt,

        @NotNull
        @Schema(example = "2023-01-01T00:00:00Z", description = "Data e hora quando o aluno foi criado")
        Instant createdAt,

        @Schema(nullable = true,example = "2023-01-01T00:00:00Z",description = "Data e hora quando o aluno foi atualizado")
        Instant updatedAt
        ) {

}
