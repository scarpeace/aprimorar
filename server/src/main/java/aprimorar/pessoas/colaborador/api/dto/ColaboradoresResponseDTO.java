package aprimorar.pessoas.colaborador.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

@Schema(description = "Resumo e listagem paginada de colaboradores")
public record ColaboradoresResponseDTO(
    @Schema(description = "Total de colaboradores ativos", example = "24")
    Long totalColaboradoresAtivos,
    @Schema(description = "Pagina de colaboradores conforme filtros aplicados")
    Page<ColaboradorResponseDTO> colaboradores
) {
}
