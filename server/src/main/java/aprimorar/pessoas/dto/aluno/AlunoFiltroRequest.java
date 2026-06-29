package aprimorar.pessoas.dto.aluno;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Filtros opcionais para listar alunos")
public record AlunoFiltroRequest(
    @Schema(description = "Nome do aluno", nullable = true)
    String nome,
    @Schema(description = "E-mail do aluno", nullable = true)
    String email,
    @Schema(description = "CPF do aluno", nullable = true)
    String cpf,
    @Schema(description = "Escola do aluno", nullable = true)
    String escola,
    @Schema(description = "Filtrar por alunos ativos", nullable = true)
    Boolean ativos
) {
}
