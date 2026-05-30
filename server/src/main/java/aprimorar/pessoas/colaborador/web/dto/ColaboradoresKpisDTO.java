package aprimorar.pessoas.colaborador.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Formato de payload para obter opções de colaboradores")
public record ColaboradoresKpisDTO(
        @Schema(description = "ID do funcionário")
        long totalColaboradores,
        @Schema(description = "Total de colaboradores ativos")
        long totalColaboradoresAtivos
) {
}
