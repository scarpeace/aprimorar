package aprimorar.pessoas.dto.responsavel;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Filtros opcionais para listar responsáveis")
public record ResponsavelFiltroRequest(
    @Schema(description = "Nome do responsável", nullable = true)
    String nome,
    @Schema(description = "E-mail do responsável", nullable = true)
    String email,
    @Schema(description = "CPF do responsável", nullable = true)
    String cpf
) {
}
