package aprimorar.instituicao.alunos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import aprimorar.instituicao.alunos.domain.AlunoEntity;
import aprimorar.instituicao.alunos.domain.Responsavel;
import aprimorar.instituicao.alunos.domain.exception.AlunoNaoEncontradoException;
import aprimorar.instituicao.alunos.repository.AlunoRepository;
import aprimorar.instituicao.alunos.service.AlunoServiceImpl;
import aprimorar.instituicao.alunos.web.dto.aluno.AlunoFiltroRequest;
import aprimorar.instituicao.common.domain.Endereco;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AlunoServiceImplQueryTest {

    @Mock
    private AlunoRepository alunoRepo;

    private AlunoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AlunoServiceImpl(alunoRepo);
    }

    @Test
    void shouldListAlunos() {
        var aluno = aluno("Ana Silva");

        when(alunoRepo.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of(aluno));

        var response = service.listAlunos();

        assertEquals(1, response.size());
        assertEquals(aluno.getId(), response.getFirst().id());
        assertEquals("Ana Silva", response.getFirst().nome());
    }

    @Test
    void shouldFindAlunoById() {
        var id = UUID.randomUUID();
        var aluno = aluno("Ana Silva");
        setId(aluno, id);

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));

        var response = service.findAlunoById(id);

        assertEquals(id, response.id());
        assertEquals("Ana Silva", response.nome());
        assertEquals("João Pereira", response.responsavel().nome());
    }

    @Test
    void shouldCheckAlunoExists() {
        var id = UUID.randomUUID();
        var aluno = aluno("Ana Silva");
        setId(aluno, id);

        when(alunoRepo.existsById(id)).thenReturn(true);

        var response = service.existsById(id);

        assertTrue(response);
    }

    @Test
    void shouldFindAlunoEntityById() {
        var id = UUID.randomUUID();
        var aluno = aluno("Ana Silva");
        setId(aluno, id);

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));

        var response = service.findEntityById(id);

        assertTrue(response.isPresent());
        assertEquals(aluno, response.orElseThrow());
    }

    @Test
    void shouldReturnEmptyWhenAlunoEntityDoesNotExist() {
        var id = UUID.randomUUID();

        when(alunoRepo.findById(id)).thenReturn(Optional.empty());

        var response = service.findEntityById(id);

        assertTrue(response.isEmpty());
    }

    @Test
    void shouldThrowWhenFindAlunoByIdDoesNotExist() {
        var id = UUID.randomUUID();

        when(alunoRepo.findById(id)).thenReturn(Optional.empty());

        var ex = assertThrows(AlunoNaoEncontradoException.class, () -> service.findAlunoById(id));

        assertEquals("Aluno não encontrado no banco de dados", ex.getMessage());
    }

    private static AlunoEntity aluno(String nome) {
        var aluno = new AlunoEntity(
            nome,
            LocalDate.of(2010, 1, 1),
            "61999999999",
            "12345678900",
            "ana@example.com",
            "Colégio Aprimorar",
            responsavel(),
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000000", "Apto 1")
        );
        setId(aluno, UUID.randomUUID());
        return aluno;
    }

    private static Responsavel responsavel() {
        return new Responsavel(
            "João Pereira",
            "61988887777",
            "98765432100",
            "joao@example.com"
        );
    }

    private static void setId(AlunoEntity aluno, UUID id) {
        ReflectionTestUtils.setField(aluno, "id", id);
    }

}
