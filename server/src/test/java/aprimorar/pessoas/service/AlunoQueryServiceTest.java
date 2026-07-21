package aprimorar.pessoas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import aprimorar.exception.BusinessException;
import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.domain.Endereco;
import aprimorar.pessoas.domain.Responsavel;
import aprimorar.pessoas.dto.aluno.AlunoFiltroRequest;
import aprimorar.pessoas.repository.AlunoRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AlunoQueryServiceTest {

    private static final UUID GHOST_ID = UUID.fromString("00000000-0000-4000-8000-000000000002");
    private static final UUID RESPONSAVEL_ID = UUID.fromString("11111111-1111-4111-8111-111111111111");

    @Mock
    private AlunoRepository alunoRepo;

    private AlunoQueryService service;

    @BeforeEach
    void setUp() {
        service = new AlunoQueryService(alunoRepo, GHOST_ID.toString());
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
        assertEquals(RESPONSAVEL_ID, response.getContent().getFirst().responsavelId());
    }

    @Test
    void shouldGetAlunosKpis() {
        when(alunoRepo.countByIdNot(GHOST_ID)).thenReturn(5L);
        when(alunoRepo.countByActiveTrueAndIdNot(GHOST_ID)).thenReturn(4L);

        var response = service.getAlunosKpis();

        assertEquals(5, response.totalAlunos());
        assertEquals(4, response.totalAlunosAtivos());
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
        assertEquals(RESPONSAVEL_ID, response.responsavelId());
    }

    @Test
    void shouldThrowWhenFindAlunoByIdDoesNotExist() {
        var id = UUID.randomUUID();

        when(alunoRepo.findById(id)).thenReturn(Optional.empty());

        var ex = assertThrows(BusinessException.class, () -> service.findAlunoById(id));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Aluno não encontrado no banco de dados", ex.getMessage());
    }

    @Test
    void shouldGetAlunosByResponsavelId() {
        var aluno = aluno("Ana Silva");

        when(alunoRepo.findAllByResponsavelId(RESPONSAVEL_ID)).thenReturn(List.of(aluno));

        var response = service.getAlunosByResponsavelId(RESPONSAVEL_ID);

        assertEquals(1, response.size());
        assertEquals(aluno.getId(), response.getFirst().id());
        assertEquals("Ana Silva", response.getFirst().nome());
    }

    private static Aluno aluno(String nome) {
        var aluno = new Aluno(
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
        var responsavel = new Responsavel(
            "João Pereira",
            LocalDate.of(1980, 5, 21),
            "61988887777",
            "98765432100",
            "joao@example.com"
        );
        setId(responsavel, RESPONSAVEL_ID);
        return responsavel;
    }

    private static void setId(Aluno aluno, UUID id) {
        ReflectionTestUtils.setField(aluno, "id", id);
    }

    private static void setId(Responsavel responsavel, UUID id) {
        ReflectionTestUtils.setField(responsavel, "id", id);
    }
}
