package aprimorar.pessoas.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import aprimorar.pessoas.dto.responsavel.ResponsavelRequestDTO;

class ResponsavelRequestDTOTest {

    @Test
    void shouldConvertToResponsavelEntity() {
        var dto = new ResponsavelRequestDTO(
            "João Silva",
            "JOAO.SILVA@EXAMPLE.COM",
            "(11) 99999-9999",
            LocalDate.of(1990, 1, 1),
            "123.456.789-01"
        );

        var entity = dto.toEntity();

        assertEquals("João Silva", entity.getNome());
        assertEquals(LocalDate.of(1990, 1, 1), entity.getDataNascimento());
        assertEquals("11999999999", entity.getTelefone());
        assertEquals("12345678901", entity.getCpf());
        assertEquals("joao.silva@example.com", entity.getEmail());
    }
}
