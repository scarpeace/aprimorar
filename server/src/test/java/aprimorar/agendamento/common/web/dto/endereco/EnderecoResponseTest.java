package aprimorar.agendamento.common.web.dto.endereco;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import aprimorar.agendamento.common.domain.Endereco;

class EnderecoResponseTest {

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

        var dto = EnderecoResponse.toDto(endereco);

        assertEquals("Rua A", dto.rua());
        assertEquals("10", dto.numero());
        assertEquals("Centro", dto.bairro());
        assertEquals("Brasilia", dto.cidade());
        assertEquals("DF", dto.estado());
        assertEquals("70000-000", dto.cep());
        assertEquals("Apto 1", dto.complemento());
    }
}
