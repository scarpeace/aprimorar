package aprimorar.pessoas.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
            validResponsavel(),
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000-000", "Apto 1")
        );

        assertEquals("Ana Silva", aluno.getNome());
        assertEquals(LocalDate.of(2000, 1, 1), aluno.getDataNascimento());
        assertEquals("(61) 99999-9999", aluno.getTelefone());
        assertEquals("123.456.789-00", aluno.getCpf());
        assertEquals("ANA.SILVA@EXAMPLE.COM", aluno.getEmail());
        assertEquals("Colégio Aprimorar", aluno.getEscola());
        assertEquals("João Silva", aluno.getResponsavel().getNome());
        assertTrue(aluno.getActive());
        assertEquals("70000-000", aluno.getEndereco().getCep());
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
            validResponsavel(),
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000-000", "Apto 1")
        );

        aluno.update(
            "Maria Silva",
            LocalDate.of(2001, 2, 2),
            "(21) 98888-7777",
            "maria.silva@example.com",
            "Escola Nova",
            new Responsavel(
                "Maria Ramos",
                LocalDate.of(1980, 3, 3),
                "(21) 97777-6666",
                "987.654.321-00",
                "maria.ramos@example.com"
            ),
            new Endereco("Rua B", "20", "Bairro", "Rio", "RJ", "20000-000", "Sala 2")
        );

        assertEquals("Maria Silva", aluno.getNome());
        assertEquals(LocalDate.of(2001, 2, 2), aluno.getDataNascimento());
        assertEquals("(21) 98888-7777", aluno.getTelefone());
        assertEquals("maria.silva@example.com", aluno.getEmail());
        assertEquals("Escola Nova", aluno.getEscola());
        assertEquals("Maria Ramos", aluno.getResponsavel().getNome());
        assertEquals("123.456.789-00", aluno.getCpf());
        assertEquals("20000-000", aluno.getEndereco().getCep());
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

    private static Aluno validAluno() {
        return new Aluno(
            "Ana Silva",
            LocalDate.of(2000, 1, 1),
            "(61) 99999-9999",
            "123.456.789-00",
            "ana.silva@example.com",
            "Colégio Aprimorar",
            validResponsavel(),
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000-000", "Apto 1")
        );
    }

    private static Responsavel validResponsavel() {
        return new Responsavel(
            "João Silva",
            LocalDate.of(1980, 1, 1),
            "(11) 99999-9999",
            "123.456.789-01",
            "joao.silva@example.com"
        );
    }
}
