package aprimorar.pessoas.dto;

import aprimorar.pessoas.domain.Endereco;
import aprimorar.shared.MapperUtils;

public record EnderecoRequestDTO(
    String rua,
    String numero,
    String bairro,
    String cidade,
    String estado,
    String cep,
    String complemento
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
