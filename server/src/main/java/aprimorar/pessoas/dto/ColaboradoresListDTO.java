package aprimorar.pessoas.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Formato de payload para obter opções de colaboradores")
public record ColaboradoresListDTO(
        @NotNull
        @Schema(description = "ID do funcionário")
        UUID id,

        @NotNull
        @Schema(description = "Nome do funcionário")
        String nome
) {
}
