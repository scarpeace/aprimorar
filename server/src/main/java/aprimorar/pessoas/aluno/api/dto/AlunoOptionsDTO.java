package aprimorar.pessoas.aluno.api.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;

public record AlunoOptionsDTO(
        @NotNull UUID id,
        @NotNull String name
) {
}
