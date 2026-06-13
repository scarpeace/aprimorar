package aprimorar.pessoas.api;

import aprimorar.pessoas.domain.Endereco;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados do endereco retornados pela API")
public record EnderecoResponseDTO(
    @NotNull String rua,
    @NotNull String numero,
    String complemento,
    @NotNull String bairro,
    @NotNull String cidade,
    @NotNull String estado,
    @NotNull String cep
) {
    public static EnderecoResponseDTO toDto(Endereco endereco) {
        return new EnderecoResponseDTO(
            endereco.getRua(),
            endereco.getNumero(),
            endereco.getComplemento(),
            endereco.getBairro(),
            endereco.getCidade(),
            endereco.getEstado(),
            endereco.getCep()
        );
    }
}
