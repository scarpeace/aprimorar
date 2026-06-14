package aprimorar.pessoas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.domain.Endereco;
import aprimorar.pessoas.dto.AlunoFiltroRequest;
import aprimorar.pessoas.repository.AlunoRepository;
import aprimorar.pessoas.shared.FuncoesColaborador;
import aprimorar.shared.exception.BusinessException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class AlunoQueryServiceTest {

    @Mock
    private AlunoRepository alunoRepo;

    @InjectMocks
    private AlunoQueryService service;

    @Test
    void shouldReturnPaginatedAlunos() {
        var pageable = PageRequest.of(0, 10);
        var filtro = new AlunoFiltroRequest("Ana", "ana@example.com", "123", "Escola", true);
        var aluno = validAluno();

        when(alunoRepo.findAll(any(Specification.class), any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(aluno), pageable, 1));

        var result = service.getAlunos(filtro, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Ana Silva", result.getContent().getFirst().nome());
        verify(alunoRepo).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void shouldReturnAlunosKpis() {
        when(alunoRepo.countByIdNot(UUID.fromString("00000000-0000-4000-8000-000000000002"))).thenReturn(10L);
        when(alunoRepo.countByActiveTrueAndIdNot(UUID.fromString("00000000-0000-4000-8000-000000000002"))).thenReturn(7L);

        var result = service.getAlunosKpis();

        assertEquals(10L, result.totalAlunos());
        assertEquals(7L, result.totalAlunosAtivos());
    }

    @Test
    void shouldListAlunos() {
        var aluno = validAluno();
        when(alunoRepo.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of(aluno));

        var result = service.listAlunos();

        assertEquals(1, result.size());
        assertEquals("Ana Silva", result.getFirst().nome());
    }

    @Test
    void shouldFindAlunoById() {
        var alunoId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var aluno = validAluno();
        setField(aluno, "id", alunoId);
        setField(aluno, "createdAt", LocalDateTime.of(2024, 1, 1, 10, 0));

        when(alunoRepo.findById(alunoId)).thenReturn(Optional.of(aluno));

        var result = service.findAlunoById(alunoId);

        assertEquals(alunoId, result.id());
        assertEquals("Ana Silva", result.nome());
    }

    @Test
    void shouldThrowWhenAlunoByIdDoesNotExist() {
        var alunoId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        when(alunoRepo.findById(alunoId)).thenReturn(Optional.empty());

        var exception = assertThrows(BusinessException.class, () -> service.findAlunoById(alunoId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Aluno não encontrado no banco de dados", exception.getMessage());
    }

    @Test
    void shouldReturnAlunosByResponsavelId() {
        var responsavelId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var aluno = validAluno();
        when(alunoRepo.findAllByResponsavelId(responsavelId)).thenReturn(List.of(aluno));

        var result = service.getAlunosByResponsavelId(responsavelId);

        assertEquals(1, result.size());
        assertEquals("Ana Silva", result.getFirst().nome());
    }

    @Test
    void shouldCheckActiveAlunosLinkedToResponsavel() {
        var responsavelId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        when(alunoRepo.existsByResponsavelIdAndActiveTrue(responsavelId)).thenReturn(true);

        var result = service.hasActiveAlunosLinkedToResponsavel(responsavelId);

        assertEquals(true, result);
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

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
