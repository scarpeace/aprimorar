package aprimorar.instituicao.alunos.web.dto.aluno;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import aprimorar.instituicao.common.web.dto.endereco.EnderecoRequestDTO;

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
            new ResponsavelRequestDTO("Carlos Silva", "carlos@example.com", "61999999998", "98765432100")
        );

        var entity = dto.toEntity();

        assertEquals("Ana Silva", entity.getNome());
        assertEquals(LocalDate.of(2000, 1, 1), entity.getDataNascimento());
        assertEquals("61999999999", entity.getTelefone());
        assertEquals("12345678900", entity.getCpf());
        assertEquals("ana.silva@example.com", entity.getEmail());
        assertEquals("Colégio Aprimorar", entity.getEscola());
        assertEquals("Carlos Silva", entity.getResponsavel().getNome());
        assertEquals("70000000", entity.getEndereco().getCep());
    }
}
