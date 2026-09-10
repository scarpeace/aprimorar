package aprimorar.pessoas.aluno;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import aprimorar.pessoas.aluno.domain.AlunoEntity;
import aprimorar.pessoas.aluno.domain.Responsavel;
import aprimorar.pessoas.aluno.domain.exception.AlunoNaoEncontradoException;
import aprimorar.pessoas.aluno.repository.AlunoRepository;
import aprimorar.pessoas.aluno.web.dto.AlunoFiltroRequest;
import aprimorar.pessoas.shared.endereco.domain.Endereco;
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
    void shouldGetAlunos() {
        var aluno = aluno("Ana Silva");
        var pageable = PageRequest.of(0, 10);

        when(alunoRepo.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(List.of(aluno), pageable, 1));

        var response = service.getAlunos(new AlunoFiltroRequest(null, null, null, null, true), pageable);

        assertEquals(1, response.getTotalElements());
        assertEquals(aluno.getId(), response.getContent().getFirst().id());
        assertEquals("Ana Silva", response.getContent().getFirst().nome());
        assertEquals("João Pereira", response.getContent().getFirst().responsavel().nome());
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
    void shouldExposeAlunoContract() {
        var id = UUID.randomUUID();
        var aluno = aluno("Ana Silva");
        setId(aluno, id);

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));

        var response = service.buscarPorId(id);

        assertEquals(id, response.id());
        assertEquals("Ana Silva", response.nome());
        assertEquals("Colégio Aprimorar", response.escola());
        assertEquals(true, response.ativo());
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
