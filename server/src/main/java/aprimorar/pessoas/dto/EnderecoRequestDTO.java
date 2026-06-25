package aprimorar.pessoas.dto;

import aprimorar.pessoas.domain.Endereco;
import aprimorar.shared.MapperUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Endereço do cadastro")
public record EnderecoRequestDTO(
    @NotBlank(message = "Informe a rua")
    @Schema(description = "Rua", example = "Avenida Paulista")
    String rua,

    @NotBlank(message = "Informe o número")
    @Schema(description = "Número", example = "1000")
    String numero,

    @Schema(description = "Complemento", example = "Sala 101", nullable = true)
    String complemento,

    @NotBlank(message = "Informe o bairro")
    @Schema(description = "Bairro", example = "Bela Vista")
    String bairro,

    @NotBlank(message = "Informe a cidade")
    @Schema(description = "Cidade", example = "São Paulo")
    String cidade,

    @NotBlank(message = "Informe a sigla do estado")
    @Size(min = 2, max = 2, message = "Use a sigla do estado")
    @Schema(description = "UF", example = "SP")
    String estado,

    @NotBlank(message = "Informe o CEP")
    @Pattern(regexp = "^\\d{8}$", message = "Use 8 dígitos no CEP")
    @Schema(description = "CEP sem pontuação", example = "01310000")
    String cep
) {

    public Endereco toEntity() {
        return new Endereco(
            rua,
            numero,
            bairro,
            cidade,
            estado,
            MapperUtils.normalizeZip(cep),
            complemento
        );
    }

    public void build(Endereco endereco) {
        endereco.setRua(rua);
        endereco.setNumero(numero);
        endereco.setBairro(bairro);
        endereco.setCidade(cidade);
        endereco.setEstado(estado);
        endereco.setCep(MapperUtils.normalizeZip(cep));
        endereco.setComplemento(complemento);
    }

}
