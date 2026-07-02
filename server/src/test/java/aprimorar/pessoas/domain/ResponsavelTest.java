package aprimorar.pessoas.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ResponsavelTest {

    @Test
    void shouldCreateResponsavelNormalizingData() {
        var responsavel = new Responsavel(
            "João Silva",
            LocalDate.of(1990, 1, 1),
            "(11) 99999-9999",
            "123.456.789-01",
            "JOAO.SILVA@EXAMPLE.COM"
        );

        assertEquals("João Silva", responsavel.getNome());
        assertEquals(LocalDate.of(1990, 1, 1), responsavel.getDataNascimento());
        assertEquals("(11) 99999-9999", responsavel.getTelefone());
        assertEquals("123.456.789-01", responsavel.getCpf());
        assertEquals("JOAO.SILVA@EXAMPLE.COM", responsavel.getEmail());
    }

    @Test
    void shouldUpdateResponsavelFields() {
        var responsavel = new Responsavel(
            "João Silva",
            LocalDate.of(1990, 1, 1),
            "(11) 99999-9999",
            "123.456.789-01",
            "joao.silva@example.com"
        );

        responsavel.update(
            "Maria Silva",
            LocalDate.of(1992, 2, 2),
            "(21) 98888-7777",
            "Maria.Silva@Example.com"
        );

        assertEquals("Maria Silva", responsavel.getNome());
        assertEquals(LocalDate.of(1992, 2, 2), responsavel.getDataNascimento());
        assertEquals("(21) 98888-7777", responsavel.getTelefone());
        assertEquals("Maria.Silva@Example.com", responsavel.getEmail());
        assertEquals("123.456.789-01", responsavel.getCpf());
    }

    @Test
    void shouldSetCreatedAtOnPrePersist() {
        var responsavel = new Responsavel(
            "João Silva",
            LocalDate.of(1990, 1, 1),
            "(11) 99999-9999",
            "123.456.789-01",
            "joao.silva@example.com"
        );

        responsavel.prePersist();

        assertTrue(responsavel.getCreatedAt() != null);
        assertTrue(responsavel.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void shouldSetUpdatedAtOnPreUpdate() {
        var responsavel = new Responsavel(
            "João Silva",
            LocalDate.of(1990, 1, 1),
            "(11) 99999-9999",
            "123.456.789-01",
            "joao.silva@example.com"
        );

        responsavel.preUpdate();

        assertTrue(responsavel.getUpdatedAt() != null);
        assertTrue(responsavel.getUpdatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
