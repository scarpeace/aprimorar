package aprimorar.pessoas.colaborador.api.dto;

import aprimorar.shared.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo e listagem paginada de colaboradores")
public record ColaboradoresResponseDTO(
    @Schema(description = "Total de colaboradores ativos", example = "24")
    Long totalColaboradoresAtivos,
    @Schema(description = "Pagina de colaboradores conforme filtros aplicados")
    PageDTO<ColaboradorResponseDTO> colaboradores
) {
}
