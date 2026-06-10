package aprimorar.pessoas.dto;

public record AlunoFiltroRequest(
    String nome,
    String email,
    String cpf,
    String escola,
    Boolean ativos
) {
}
