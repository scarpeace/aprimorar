package aprimorar.pessoas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.domain.Endereco;
import aprimorar.pessoas.dto.AlunoRequestDTO;
import aprimorar.pessoas.dto.EnderecoRequestDTO;
import aprimorar.pessoas.api.AlunoDeletedEvent;
import aprimorar.pessoas.api.ArchiveAlunoVerificationEvent;
import aprimorar.pessoas.api.DeleteAlunoVerificationEvent;
import aprimorar.pessoas.api.ResponsavelQueryApi;
import aprimorar.pessoas.repository.AlunoRepository;
import aprimorar.pessoas.shared.FuncoesColaborador;
import aprimorar.shared.exception.BusinessException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class AlunoMutationServiceTest {

    @Mock
    private AlunoRepository alunoRepo;

    @Mock
    private ResponsavelQueryApi responsavelQueryApi;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AlunoMutationService service;

    @Test
    void shouldCreateAlunoWhenCpfAndEmailAreAvailable() {
        var dto = validRequest();

        when(alunoRepo.existsByCpf("12345678900")).thenReturn(false);
        when(alunoRepo.existsByEmail("ana.silva@example.com")).thenReturn(false);
        when(alunoRepo.save(any(Aluno.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(responsavelQueryApi.findResponsavelById(dto.responsavelId())).thenReturn(null);

        var response = service.createAluno(dto);

        assertEquals("Ana Silva", response.nome());
        assertEquals("12345678900", response.cpf());
        assertEquals("61999999999", response.telefone());
        assertEquals("ana.silva@example.com", response.email());
        verify(alunoRepo).save(any(Aluno.class));
    }

    @Test
    void shouldRejectCreateWhenCpfAlreadyExists() {
        when(alunoRepo.existsByCpf("12345678900")).thenReturn(true);

        var exception = assertThrows(BusinessException.class, () -> service.createAluno(validRequest()));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Já existe um aluno cadastrado com este CPF.", exception.getMessage());
        verify(alunoRepo, never()).save(any());
    }

    @Test
    void shouldRejectCreateWhenEmailAlreadyExists() {
        when(alunoRepo.existsByCpf("12345678900")).thenReturn(false);
        when(alunoRepo.existsByEmail("ana.silva@example.com")).thenReturn(true);

        var exception = assertThrows(BusinessException.class, () -> service.createAluno(validRequest()));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Já existe um aluno cadastrado com este e-mail.", exception.getMessage());
    }

    @Test
    void shouldUpdateAlunoWhenCpfAndEmailAreAvailable() {
        var alunoId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var existing = validAluno();

        when(alunoRepo.findById(alunoId)).thenReturn(Optional.of(existing));
        when(alunoRepo.existsByCpfAndIdNot("98765432100", alunoId)).thenReturn(false);
        when(alunoRepo.existsByEmailAndIdNot("maria.silva@example.com", alunoId)).thenReturn(false);
        when(responsavelQueryApi.findResponsavelById(UUID.fromString("223e4567-e89b-12d3-a456-426614174000"))).thenReturn(null);

        var response = service.updateAluno(alunoId, validUpdateRequest());

        assertEquals("Maria Silva", response.nome());
        assertEquals("12345678900", response.cpf());
        assertEquals("21988887777", response.telefone());
        assertEquals("maria.silva@example.com", response.email());
        verify(alunoRepo, never()).save(any());
    }

    @Test
    void shouldRejectUpdateWhenColaboradorDoesNotExist() {
        var alunoId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        when(alunoRepo.findById(alunoId)).thenReturn(Optional.empty());

        var exception = assertThrows(BusinessException.class, () -> service.updateAluno(alunoId, validUpdateRequest()));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Aluno não encontrado no banco de dados", exception.getMessage());
    }

    @Test
    void shouldArchiveAluno() {
        var alunoId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var aluno = validAluno();
        when(alunoRepo.findById(alunoId)).thenReturn(Optional.of(aluno));

        service.archiveAluno(alunoId);

        assertEquals(false, aluno.getActive());
        verify(eventPublisher).publishEvent(any(ArchiveAlunoVerificationEvent.class));
    }

    @Test
    void shouldUnarchiveAluno() {
        var alunoId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var aluno = validAluno();
        aluno.archive();
        when(alunoRepo.findById(alunoId)).thenReturn(Optional.of(aluno));

        service.unarchiveAluno(alunoId);

        assertEquals(true, aluno.getActive());
    }

    @Test
    void shouldDeleteAluno() {
        var alunoId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var aluno = validAluno();
        when(alunoRepo.findById(alunoId)).thenReturn(Optional.of(aluno));

        service.deleteAluno(alunoId);

        verify(eventPublisher).publishEvent(any(DeleteAlunoVerificationEvent.class));
        verify(eventPublisher).publishEvent(any(AlunoDeletedEvent.class));
        verify(alunoRepo).delete(aluno);
    }

    @Test
    void shouldRejectUpdateWhenAlunoIsGhost() {
        var ghostId = UUID.fromString("00000000-0000-4000-8000-000000000002");
        var aluno = validAluno();
        when(alunoRepo.findById(ghostId)).thenReturn(Optional.of(aluno));

        var exception = assertThrows(BusinessException.class, () -> service.updateAluno(ghostId, validUpdateRequest()));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    private AlunoRequestDTO validRequest() {
        return new AlunoRequestDTO(
            "Ana Silva",
            LocalDate.of(2000, 1, 1),
            "123.456.789-00",
            "Colégio Aprimorar",
            "(61) 99999-9999",
            "ANA.SILVA@EXAMPLE.COM",
            new EnderecoRequestDTO("Rua A", "10", "Centro", "Brasilia", "DF", "70000-000", "Apto 1"),
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
        );
    }

    private AlunoRequestDTO validUpdateRequest() {
        return new AlunoRequestDTO(
            "Maria Silva",
            LocalDate.of(2001, 2, 2),
            "987.654.321-00",
            "Escola Nova",
            "(21) 98888-7777",
            "Maria.Silva@Example.com",
            new EnderecoRequestDTO("Rua B", "20", "Bairro", "Rio", "RJ", "20000-000", "Sala 2"),
            UUID.fromString("223e4567-e89b-12d3-a456-426614174000")
        );
    }

    private Aluno validAluno() {
        return new Aluno(
            "Ana Silva",
            LocalDate.of(2000, 1, 1),
            "61999999999",
            "12345678900",
            "ana.silva@example.com",
            "Colégio Aprimorar",
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000000", "Apto 1")
        );
    }
}
