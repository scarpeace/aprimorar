package aprimorar.pessoas.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlunoTest {

    @Test
    void shouldCreateAlunoNormalizingData() {
        var aluno = new Aluno(
            "Ana Silva",
            LocalDate.of(2000, 1, 1),
            "(61) 99999-9999",
            "123.456.789-00",
            "ANA.SILVA@EXAMPLE.COM",
            "Colégio Aprimorar",
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000-000", "Apto 1")
        );

        assertEquals("Ana Silva", aluno.getNome());
        assertEquals(LocalDate.of(2000, 1, 1), aluno.getDataNascimento());
        assertEquals("61999999999", aluno.getTelefone());
        assertEquals("12345678900", aluno.getCpf());
        assertEquals("ana.silva@example.com", aluno.getEmail());
        assertEquals("Colégio Aprimorar", aluno.getEscola());
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), aluno.getResponsavelId());
        assertTrue(aluno.getActive());
        assertEquals("70000000", aluno.getEndereco().getCep());
    }

    @Test
    void shouldUpdateAlunoFields() {
        var aluno = new Aluno(
            "Ana Silva",
            LocalDate.of(2000, 1, 1),
            "(61) 99999-9999",
            "123.456.789-00",
            "ana.silva@example.com",
            "Colégio Aprimorar",
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000-000", "Apto 1")
        );

        aluno.update(
            "Maria Silva",
            LocalDate.of(2001, 2, 2),
            "(21) 98888-7777",
            "maria.silva@example.com",
            "Escola Nova",
            UUID.fromString("223e4567-e89b-12d3-a456-426614174000"),
            new Endereco("Rua B", "20", "Bairro", "Rio", "RJ", "20000-000", "Sala 2")
        );

        assertEquals("Maria Silva", aluno.getNome());
        assertEquals(LocalDate.of(2001, 2, 2), aluno.getDataNascimento());
        assertEquals("21988887777", aluno.getTelefone());
        assertEquals("maria.silva@example.com", aluno.getEmail());
        assertEquals("Escola Nova", aluno.getEscola());
        assertEquals(UUID.fromString("223e4567-e89b-12d3-a456-426614174000"), aluno.getResponsavelId());
        assertEquals("12345678900", aluno.getCpf());
        assertEquals("20000000", aluno.getEndereco().getCep());
    }

    @Test
    void shouldArchiveAndUnarchiveAluno() {
        var aluno = validAluno();

        aluno.archive();
        assertFalse(aluno.getActive());

        aluno.unarchive();
        assertTrue(aluno.getActive());
    }

    @Test
    void shouldSetCreatedAtAndUpdatedAt() {
        var aluno = validAluno();

        aluno.prePersist();
        aluno.preUpdate();

        assertTrue(aluno.getCreatedAt() != null);
        assertTrue(aluno.getUpdatedAt() != null);
        assertTrue(aluno.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(aluno.getUpdatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void shouldRejectAlunoWithoutResponsavel() {
        var exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Aluno(
                "Ana Silva",
                LocalDate.of(2000, 1, 1),
                "(61) 99999-9999",
                "123.456.789-00",
                "ana.silva@example.com",
                "Colégio Aprimorar",
                null,
                new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000-000", "Apto 1")
            )
        );

        assertEquals("Aluno não pode ser cadastrado sem um responsável", exception.getMessage());
    }

    private static Aluno validAluno() {
        return new Aluno(
            "Ana Silva",
            LocalDate.of(2000, 1, 1),
            "(61) 99999-9999",
            "123.456.789-00",
            "ana.silva@example.com",
            "Colégio Aprimorar",
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000-000", "Apto 1")
        );
    }
}
