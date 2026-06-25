package aprimorar.pessoas.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Filtros opcionais para listar colaboradores")
public record ColaboradorFiltroRequest(
    @Schema(description = "Nome do colaborador", nullable = true)
    String nome,
    @Schema(description = "E-mail do colaborador", nullable = true)
    String email,
    @Schema(description = "CPF do colaborador", nullable = true)
    String cpf,
    @Schema(description = "Filtrar por colaboradores ativos", nullable = true)
    Boolean ativos
) {
}
