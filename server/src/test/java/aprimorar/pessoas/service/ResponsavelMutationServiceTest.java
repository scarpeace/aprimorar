package aprimorar.pessoas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.exception.BusinessException;
import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.domain.Endereco;
import aprimorar.pessoas.domain.Responsavel;
import aprimorar.pessoas.dto.responsavel.ResponsavelRequestDTO;
import aprimorar.pessoas.repository.AlunoRepository;
import aprimorar.pessoas.repository.ResponsavelRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ResponsavelMutationServiceTest {

    @Mock
    private ResponsavelRepository responsavelRepo;

    @Mock
    private AlunoRepository alunoRepo;

    private ResponsavelMutationService service;

    @BeforeEach
    void setUp() {
        service = new ResponsavelMutationService(responsavelRepo, alunoRepo);
    }

    @Test
    void shouldCreateResponsavel() {
        var dto = responsavelRequest();
        var saved = dto.toEntity();
        setId(saved, UUID.randomUUID());

        when(responsavelRepo.existsByCpf("12345678900")).thenReturn(false);
        when(responsavelRepo.existsByEmail("joao@example.com")).thenReturn(false);
        when(responsavelRepo.save(any(Responsavel.class))).thenReturn(saved);

        var response = service.createResponsavel(dto);

        assertEquals(saved.getId(), response.id());
        assertEquals("João Pereira", response.nome());

        ArgumentCaptor<Responsavel> captor = ArgumentCaptor.forClass(Responsavel.class);
        verify(responsavelRepo).save(captor.capture());
        assertEquals("12345678900", captor.getValue().getCpf());
        assertEquals("joao@example.com", captor.getValue().getEmail());
        assertEquals("61999999999", captor.getValue().getTelefone());
    }

    @Test
    void shouldThrowWhenCreateAndCpfAlreadyExists() {
        var dto = responsavelRequest();

        when(responsavelRepo.existsByCpf("12345678900")).thenReturn(true);

        var ex = assertThrows(BusinessException.class, () -> service.createResponsavel(dto));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("Já existe um responsável cadastrado com este CPF.", ex.getMessage());
        verify(responsavelRepo, never()).save(any());
    }

    @Test
    void shouldThrowWhenCreateAndEmailAlreadyExists() {
        var dto = responsavelRequest();

        when(responsavelRepo.existsByCpf("12345678900")).thenReturn(false);
        when(responsavelRepo.existsByEmail("joao@example.com")).thenReturn(true);

        var ex = assertThrows(BusinessException.class, () -> service.createResponsavel(dto));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("Já existe um responsável cadastrado com este e-mail.", ex.getMessage());
        verify(responsavelRepo, never()).save(any());
    }

    @Test
    void shouldUpdateResponsavel() {
        UUID id = UUID.randomUUID();
        var responsavel = responsavel();
        setId(responsavel, id);

        var dto = new ResponsavelRequestDTO(
            "Maria Souza",
            "maria@example.com",
            "(61) 98888-7777",
            LocalDate.of(1985, 8, 10),
            "123.456.789-00"
        );

        when(responsavelRepo.findById(id)).thenReturn(Optional.of(responsavel));
        when(responsavelRepo.existsByCpfAndIdNot("12345678900", id)).thenReturn(false);
        when(responsavelRepo.existsByEmailAndIdNot("maria@example.com", id)).thenReturn(false);

        var response = service.updateResponsavel(id, dto);

        assertEquals("Maria Souza", responsavel.getNome());
        assertEquals("maria@example.com", responsavel.getEmail());
        assertEquals("61988887777", responsavel.getTelefone());
        assertEquals("Maria Souza", response.nome());
    }

    @Test
    void shouldThrowWhenUpdateAndResponsavelDoesNotExist() {
        UUID id = UUID.randomUUID();
        var request = responsavelRequest();

        when(responsavelRepo.findById(id)).thenReturn(Optional.empty());

        var ex = assertThrows(BusinessException.class, () -> service.updateResponsavel(id, request));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Responsável não encontrado no banco de dados", ex.getMessage());
    }

    @Test
    void shouldThrowWhenUpdateAndCpfAlreadyUsed() {
        UUID id = UUID.randomUUID();
        var responsavel = responsavel();
        setId(responsavel, id);

        when(responsavelRepo.findById(id)).thenReturn(Optional.of(responsavel));
        when(responsavelRepo.existsByCpfAndIdNot("12345678900", id)).thenReturn(true);

        var request = responsavelRequest();
        var ex = assertThrows(BusinessException.class, () -> service.updateResponsavel(id, request));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("Já existe um responsável utilizando este CPF.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenUpdateAndEmailAlreadyUsed() {
        UUID id = UUID.randomUUID();
        var responsavel = responsavel();
        setId(responsavel, id);

        when(responsavelRepo.findById(id)).thenReturn(Optional.of(responsavel));
        when(responsavelRepo.existsByCpfAndIdNot("12345678900", id)).thenReturn(false);
        when(responsavelRepo.existsByEmailAndIdNot("joao@example.com", id)).thenReturn(true);

        var request = responsavelRequest();
        var ex = assertThrows(BusinessException.class, () -> service.updateResponsavel(id, request));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("Já existe um responsável utilizando este e-mail.", ex.getMessage());
    }

    @Test
    void shouldDeleteResponsavel() {
        UUID id = UUID.randomUUID();
        var responsavel = responsavel();
        setId(responsavel, id);

        when(responsavelRepo.findById(id)).thenReturn(Optional.of(responsavel));
        when(alunoRepo.findAllByResponsavelId(id)).thenReturn(List.of());

        service.deleteResponsavel(id);

        verify(responsavelRepo).delete(responsavel);
    }

    @Test
    void shouldThrowWhenDeleteAndResponsavelDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(responsavelRepo.findById(id)).thenReturn(Optional.empty());

        var ex = assertThrows(BusinessException.class, () -> service.deleteResponsavel(id));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Responsável não encontrado no banco de dados", ex.getMessage());
        verify(responsavelRepo, never()).delete(any(Responsavel.class));
    }

    @Test
    void shouldThrowWhenDeleteAndHasLinkedAlunos() {
        UUID id = UUID.randomUUID();
        var responsavel = responsavel();
        setId(responsavel, id);

        when(responsavelRepo.findById(id)).thenReturn(Optional.of(responsavel));
        when(alunoRepo.findAllByResponsavelId(id)).thenReturn(List.of(aluno(responsavel)));

        var ex = assertThrows(BusinessException.class, () -> service.deleteResponsavel(id));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("Este responsável possui alunos vinculados. Exclua os alunos antes de excluir o responsável.", ex.getMessage());
        verify(responsavelRepo, never()).delete(any(Responsavel.class));
    }

    private static ResponsavelRequestDTO responsavelRequest() {
        return new ResponsavelRequestDTO(
            "João Pereira",
            "joao@example.com",
            "(61) 99999-9999",
            LocalDate.of(1980, 5, 21),
            "123.456.789-00"
        );
    }

    private static Responsavel responsavel() {
        return new Responsavel(
            "João Pereira",
            LocalDate.of(1980, 5, 21),
            "61999999999",
            "12345678900",
            "joao@example.com"
        );
    }

    private static Aluno aluno(Responsavel responsavel) {
        return new Aluno(
            "Ana Silva",
            LocalDate.of(2010, 1, 1),
            "61988887777",
            "98765432100",
            "ana@example.com",
            "Colégio Aprimorar",
            responsavel,
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000000", "Apto 1")
        );
    }

    private static void setId(Responsavel responsavel, UUID id) {
        ReflectionTestUtils.setField(responsavel, "id", id);
    }
}
