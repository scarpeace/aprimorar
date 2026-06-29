package aprimorar.pessoas.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aprimorar.pessoas.dto.aluno.AlunoRequestDTO;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlunoRequestDTOTest {

    @Test
    void shouldConvertToAlunoEntity() {
        var dto = new AlunoRequestDTO(
            "Ana Silva",
            LocalDate.of(2000, 1, 1),
            "123.456.789-00",
            "Colégio Aprimorar",
            "(61) 99999-9999",
            "ANA.SILVA@EXAMPLE.COM",
            new EnderecoRequestDTO("Rua A", "10", "Apto 1", "Centro", "Brasilia", "DF", "70000-000"),
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
        );

        var entity = dto.toEntity();

        assertEquals("Ana Silva", entity.getNome());
        assertEquals(LocalDate.of(2000, 1, 1), entity.getDataNascimento());
        assertEquals("61999999999", entity.getTelefone());
        assertEquals("12345678900", entity.getCpf());
        assertEquals("ana.silva@example.com", entity.getEmail());
        assertEquals("Colégio Aprimorar", entity.getEscola());
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), entity.getResponsavelId());
        assertEquals("70000000", entity.getEndereco().getCep());
    }
}
