package aprimorar.pessoas.aluno.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Opcao simplificada de aluno para seletores")
public record AlunosListResponseDTO(
        @NotNull
        @Schema(description = "Identificador unico do aluno", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,
        @NotNull
        @Schema(description = "Nome completo do aluno", example = "Maria Silva")
        String name
) {
}
