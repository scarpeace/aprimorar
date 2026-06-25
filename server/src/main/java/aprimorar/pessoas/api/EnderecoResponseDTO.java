package aprimorar.pessoas.api;

import aprimorar.pessoas.domain.Endereco;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados do endereco retornados pela API")
public record EnderecoResponseDTO(
    @NotBlank(message = "Rua não pode ficar vazia")
    @Schema(description = "Rua", example = "Avenida Paulista")
    String rua,

    @NotBlank(message = "Número não pode ficar vazio")
    @Schema(description = "Número", example = "1000")
    String numero,

    @Schema(description = "Complemento", example = "Sala 101", nullable = true)
    String complemento,

    @NotBlank(message = "Bairro não pode ficar vazio")
    @Schema(description = "Bairro", example = "Bela Vista")
    String bairro,

    @NotBlank(message = "Cidade não pode ficar vazia")
    @Schema(description = "Cidade", example = "São Paulo")
    String cidade,

    @NotBlank(message = "Estado não pode ficar vazio")
    @Schema(description = "UF", example = "SP")
    String estado,

    @NotBlank(message = "CEP não pode ficar vazio")
    @Schema(description = "CEP sem pontuação", example = "01310000")
    String cep
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
