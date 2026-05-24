package aprimorar.pessoas.colaborador.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo quantitativo dos colaboradores cadastrados")
public record ColaboradorCountSummaryDTO(
    @Schema(description = "Total de colaboradores ativos")
    long activeEmployees,

    @Schema(description = "Total de colaboradores cadastrados desde o inicio")
    long totalEmployees
) {
}
