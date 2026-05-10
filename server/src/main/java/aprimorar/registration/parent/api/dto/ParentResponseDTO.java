package aprimorar.registration.parent.api.dto;

import java.time.Instant;
import java.time.LocalDate;
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

        @Schema(description = "Data de nascimento do responsável", example = "1990-01-01", nullable = true)
        LocalDate birthdate,

        @NotNull
        @Schema(description = "CPF do responsável", example = "12345678901")
        String cpf,

        @NotNull
        @Schema(description = "Contato do responsável", example = "11999999999")
        String contact,

        @NotNull
        @Schema(description = "Email do responsável", example = "email@email.com")
        String email,

        @Schema(description = "Pix do Responsável", example = "email@email.com", nullable = true)
        String pix,

        @Schema(nullable = true, example = "true", description = "Indica se o responsável está ativo")
        Boolean active,

        @NotNull
        @Schema(description = "Data e hora quando o aluno foi criado",example = "2023-01-01T00:00:00Z")
        Instant createdAt,

        @Schema(nullable = true,description = "Data e hora quando o aluno foi atualizado", example = "2023-01-01T00:00:00Z")
        Instant updatedAt
        ) {

}
