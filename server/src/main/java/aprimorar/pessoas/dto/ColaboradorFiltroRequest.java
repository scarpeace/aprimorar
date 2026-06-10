package aprimorar.pessoas.dto;

public record ColaboradorFiltroRequest(
    String nome,
    String email,
    String cpf,
    Boolean ativos
) {
}
