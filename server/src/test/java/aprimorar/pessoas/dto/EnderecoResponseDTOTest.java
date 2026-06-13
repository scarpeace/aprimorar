package aprimorar.pessoas.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aprimorar.pessoas.api.EnderecoResponseDTO;
import aprimorar.pessoas.domain.Endereco;
import org.junit.jupiter.api.Test;

class EnderecoResponseDTOTest {

    @Test
    void shouldConvertEnderecoEntityToDto() {
        var endereco = new Endereco(
            "Rua A",
            "10",
            "Centro",
            "Brasilia",
            "DF",
            "70000-000",
            "Apto 1"
        );

        var dto = EnderecoResponseDTO.toDto(endereco);

        assertEquals("Rua A", dto.rua());
        assertEquals("10", dto.numero());
        assertEquals("Centro", dto.bairro());
        assertEquals("Brasilia", dto.cidade());
        assertEquals("DF", dto.estado());
        assertEquals("70000000", dto.cep());
        assertEquals("Apto 1", dto.complemento());
    }
}
