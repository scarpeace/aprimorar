package aprimorar.pessoas.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import aprimorar.pessoas.shared.FuncoesColaborador;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ColaboradorTest {

    @Test
    void shouldCreateColaboradorNormalizingData() {
        var colaborador = new Colaborador(
            "João Pereira",
            LocalDate.of(1990, 5, 21),
            "joao.pereira@example.com",
            "(61) 99999-9999",
            "123.456.789-00",
            "JOAO.PEREIRA@EXAMPLE.COM",
            FuncoesColaborador.PROFESSOR,
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000-000", "Apto 1")
        );

        assertEquals("João Pereira", colaborador.getNome());
        assertEquals(LocalDate.of(1990, 5, 21), colaborador.getDataNascimento());
        assertEquals("joao.pereira@example.com", colaborador.getPix());
        assertEquals("(61) 99999-9999", colaborador.getTelefone());
        assertEquals("123.456.789-00", colaborador.getCpf());
        assertEquals("JOAO.PEREIRA@EXAMPLE.COM", colaborador.getEmail());
        assertEquals(FuncoesColaborador.PROFESSOR, colaborador.getFuncao());
        assertTrue(colaborador.getActive());
        assertEquals("70000-000", colaborador.getEndereco().getCep());
    }

    @Test
    void shouldUpdateColaboradorFields() {
        var colaborador = new Colaborador(
            "João Pereira",
            LocalDate.of(1990, 5, 21),
            "joao.pereira@example.com",
            "(61) 99999-9999",
            "123.456.789-00",
            "joao.pereira@example.com",
            FuncoesColaborador.PROFESSOR,
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000-000", "Apto 1")
        );

        colaborador.update(
            "Maria Pereira",
            LocalDate.of(1992, 6, 10),
            "maria.pereira@example.com",
            "(21) 98888-7777",
            "maria.pereira@example.com",
            FuncoesColaborador.ADMINISTRATIVO,
            new Endereco("Rua B", "20", "Bairro", "Rio", "RJ", "20000-000", "Sala 2")
        );

        assertEquals("Maria Pereira", colaborador.getNome());
        assertEquals(LocalDate.of(1992, 6, 10), colaborador.getDataNascimento());
        assertEquals("maria.pereira@example.com", colaborador.getPix());
        assertEquals("(21) 98888-7777", colaborador.getTelefone());
        assertEquals("maria.pereira@example.com", colaborador.getEmail());
        assertEquals(FuncoesColaborador.ADMINISTRATIVO, colaborador.getFuncao());
        assertEquals("20000-000", colaborador.getEndereco().getCep());
        assertEquals("123.456.789-00", colaborador.getCpf());
    }


    @Test
    void shouldSetCreatedAtAndUpdatedAt() {
        var colaborador = new Colaborador(
            "João Pereira",
            LocalDate.of(1990, 5, 21),
            "joao.pereira@example.com",
            "(61) 99999-9999",
            "123.456.789-00",
            "joao.pereira@example.com",
            FuncoesColaborador.PROFESSOR,
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000-000", "Apto 1")
        );

        colaborador.prePersist();
        colaborador.preUpdate();

        assertTrue(colaborador.getCreatedAt() != null);
        assertTrue(colaborador.getUpdatedAt() != null);
        assertTrue(colaborador.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(colaborador.getUpdatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
