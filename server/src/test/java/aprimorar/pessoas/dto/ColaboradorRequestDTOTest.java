package aprimorar.pessoas.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aprimorar.pessoas.shared.FuncoesColaborador;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ColaboradorRequestDTOTest {

    @Test
    void shouldConvertToColaboradorEntity() {
        var dto = new ColaboradorRequestDTO(
            "João Pereira",
            LocalDate.of(1990, 5, 21),
            "joao.pereira@example.com",
            "(61) 99999-9999",
            "123.456.789-00",
            "JOAO.PEREIRA@EXAMPLE.COM",
            FuncoesColaborador.PROFESSOR,
            new EnderecoRequestDTO("Rua A", "10", "Centro", "Brasilia", "DF", "70000-000", "Apto 1")
        );

        var entity = dto.toEntity();

        assertEquals("João Pereira", entity.getNome());
        assertEquals(LocalDate.of(1990, 5, 21), entity.getDataNascimento());
        assertEquals("joao.pereira@example.com", entity.getPix());
        assertEquals("61999999999", entity.getTelefone());
        assertEquals("12345678900", entity.getCpf());
        assertEquals("joao.pereira@example.com", entity.getEmail());
        assertEquals(FuncoesColaborador.PROFESSOR, entity.getFuncao());
        assertEquals("70000000", entity.getEndereco().getCep());
    }
}
