package aprimorar.pessoas.colaborador.api.contract.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Formato de payload para obter opções de colaboradores")
public record ColaboradorOptionsDTO(
        @Schema(description = "ID do funcionário")
        UUID id,
        @Schema(description = "Nome do funcionário")
        String name
) {
}
