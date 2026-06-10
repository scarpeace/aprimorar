package aprimorar.pessoas.dto;

import aprimorar.pessoas.domain.Endereco;

public record EnderecoResponseDTO(
    String rua,
    String numero,
    String bairro,
    String cidade,
    String estado,
    String cep,
    String complemento
) {

    public static EnderecoResponseDTO toDto(Endereco endereco) {
        return new EnderecoResponseDTO(
            endereco.getRua(),
            endereco.getNumero(),
            endereco.getBairro(),
            endereco.getCidade(),
            endereco.getEstado(),
            endereco.getCep(),
            endereco.getComplemento()
        );
    }

}
