package aprimorar.pessoas.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aprimorar.pessoas.domain.Endereco;
import org.junit.jupiter.api.Test;

class EnderecoRequestDTOTest {

    @Test
    void shouldConvertToEnderecoEntity() {
        var dto = new EnderecoRequestDTO(
            "Rua A",
            "10",
            "Apto 1",
            "Centro",
            "Brasilia",
            "DF",
            "70000-000"
        );

        Endereco endereco = dto.toEntity();

        assertEquals("Rua A", endereco.getRua());
        assertEquals("10", endereco.getNumero());
        assertEquals("Centro", endereco.getBairro());
        assertEquals("Brasilia", endereco.getCidade());
        assertEquals("DF", endereco.getEstado());
        assertEquals("70000000", endereco.getCep());
        assertEquals("Apto 1", endereco.getComplemento());
    }
}
