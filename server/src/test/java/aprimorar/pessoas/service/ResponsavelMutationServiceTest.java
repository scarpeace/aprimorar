package aprimorar.pessoas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.pessoas.domain.Responsavel;
import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.dto.ResponsavelRequestDTO;
import aprimorar.pessoas.dto.ResponsavelResponseDTO;
import aprimorar.pessoas.repository.AlunoRepository;
import aprimorar.pessoas.repository.ResponsavelRepository;
import aprimorar.shared.exception.BusinessException;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class ResponsavelMutationServiceTest {

    @Mock
    private ResponsavelRepository responsavelRepo;

    @Mock
    private AlunoRepository alunoRepo;

    @InjectMocks
    private ResponsavelMutationService service;

    @Test
    void shouldCreateResponsavelWhenCpfAndEmailAreAvailable() {
        var dto = new ResponsavelRequestDTO(
            "João Silva",
            "JOAO.SILVA@EXAMPLE.COM",
            "(11) 99999-9999",
            LocalDate.of(1990, 1, 1),
            "123.456.789-01"
        );

        var saved = new Responsavel(
            "João Silva",
            LocalDate.of(1990, 1, 1),
            "(11) 99999-9999",
            "123.456.789-01",
            "joao.silva@example.com"
        );

        when(responsavelRepo.existsByCpf("12345678901")).thenReturn(false);
        when(responsavelRepo.existsByEmail("joao.silva@example.com")).thenReturn(false);
        when(responsavelRepo.save(any(Responsavel.class))).thenReturn(saved);

        ResponsavelResponseDTO response = service.createResponsavel(dto);

        assertEquals("João Silva", response.nome());
        assertEquals("12345678901", response.cpf());
        assertEquals("11999999999", response.telefone());
        assertEquals("joao.silva@example.com", response.email());
        verify(responsavelRepo).save(any(Responsavel.class));
    }

    @Test
    void shouldRejectCreateWhenCpfAlreadyExists() {
        var dto = new ResponsavelRequestDTO(
            "João Silva",
            "joao.silva@example.com",
            "(11) 99999-9999",
            LocalDate.of(1990, 1, 1),
            "123.456.789-01"
        );

        when(responsavelRepo.existsByCpf("12345678901")).thenReturn(true);

        var exception = assertThrows(BusinessException.class, () -> service.createResponsavel(dto));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Já existe um responsável cadastrado com este CPF.", exception.getMessage());
        verify(responsavelRepo, never()).save(any());
    }

    @Test
    void shouldRejectCreateWhenEmailAlreadyExists() {
        var dto = new ResponsavelRequestDTO(
            "João Silva",
            "joao.silva@example.com",
            "(11) 99999-9999",
            LocalDate.of(1990, 1, 1),
            "123.456.789-01"
        );

        when(responsavelRepo.existsByCpf("12345678901")).thenReturn(false);
        when(responsavelRepo.existsByEmail("joao.silva@example.com")).thenReturn(true);

        var exception = assertThrows(BusinessException.class, () -> service.createResponsavel(dto));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Já existe um responsável cadastrado com este e-mail.", exception.getMessage());
        verify(responsavelRepo, never()).save(any());
    }

    @Test
    void shouldUpdateResponsavelWhenCpfAndEmailAreAvailable() {
        var responsavelId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var existing = new Responsavel(
            "João Silva",
            LocalDate.of(1990, 1, 1),
            "(11) 99999-9999",
            "123.456.789-01",
            "joao.silva@example.com"
        );

        var dto = new ResponsavelRequestDTO(
            "Maria Silva",
            "MARIA.SILVA@EXAMPLE.COM",
            "(21) 98888-7777",
            LocalDate.of(1992, 2, 2),
            "987.654.321-00"
        );

        when(responsavelRepo.findById(responsavelId)).thenReturn(java.util.Optional.of(existing));
        when(responsavelRepo.existsByCpfAndIdNot("98765432100", responsavelId)).thenReturn(false);
        when(responsavelRepo.existsByEmailAndIdNot("maria.silva@example.com", responsavelId)).thenReturn(false);

        var response = service.updateResponsavel(responsavelId, dto);

        assertEquals("Maria Silva", response.nome());
        assertEquals(LocalDate.of(1992, 2, 2), response.dataNascimento());
        assertEquals("12345678901", response.cpf());
        assertEquals("21988887777", response.telefone());
        assertEquals("maria.silva@example.com", response.email());
        verify(responsavelRepo, never()).save(any());
    }

    @Test
    void shouldRejectUpdateWhenCpfAlreadyExists() {
        var responsavelId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var existing = new Responsavel(
            "João Silva",
            LocalDate.of(1990, 1, 1),
            "(11) 99999-9999",
            "123.456.789-01",
            "joao.silva@example.com"
        );

        var dto = new ResponsavelRequestDTO(
            "Maria Silva",
            "maria.silva@example.com",
            "(21) 98888-7777",
            LocalDate.of(1992, 2, 2),
            "987.654.321-00"
        );

        when(responsavelRepo.findById(responsavelId)).thenReturn(java.util.Optional.of(existing));
        when(responsavelRepo.existsByCpfAndIdNot("98765432100", responsavelId)).thenReturn(true);

        var exception = assertThrows(BusinessException.class, () -> service.updateResponsavel(responsavelId, dto));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Já existe um responsável utilizando este CPF.", exception.getMessage());
        verify(responsavelRepo, never()).save(any());
    }

    @Test
    void shouldRejectUpdateWhenEmailAlreadyExists() {
        var responsavelId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var existing = new Responsavel(
            "João Silva",
            LocalDate.of(1990, 1, 1),
            "(11) 99999-9999",
            "123.456.789-01",
            "joao.silva@example.com"
        );

        var dto = new ResponsavelRequestDTO(
            "Maria Silva",
            "maria.silva@example.com",
            "(21) 98888-7777",
            LocalDate.of(1992, 2, 2),
            "987.654.321-00"
        );

        when(responsavelRepo.findById(responsavelId)).thenReturn(java.util.Optional.of(existing));
        when(responsavelRepo.existsByCpfAndIdNot("98765432100", responsavelId)).thenReturn(false);
        when(responsavelRepo.existsByEmailAndIdNot("maria.silva@example.com", responsavelId)).thenReturn(true);

        var exception = assertThrows(BusinessException.class, () -> service.updateResponsavel(responsavelId, dto));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Já existe um responsável utilizando este e-mail.", exception.getMessage());
        verify(responsavelRepo, never()).save(any());
    }

    @Test
    void shouldRejectUpdateWhenResponsavelDoesNotExist() {
        var responsavelId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var dto = new ResponsavelRequestDTO(
            "Maria Silva",
            "maria.silva@example.com",
            "(21) 98888-7777",
            LocalDate.of(1992, 2, 2),
            "987.654.321-00"
        );

        when(responsavelRepo.findById(responsavelId)).thenReturn(java.util.Optional.empty());

        var exception = assertThrows(BusinessException.class, () -> service.updateResponsavel(responsavelId, dto));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Responsável não encontrado no banco de dados", exception.getMessage());
        verify(responsavelRepo, never()).save(any());
    }

    @Test
    void shouldDeleteResponsavelWhenNoStudentsAreLinked() {
        var responsavelId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var responsavel = new Responsavel(
            "João Silva",
            LocalDate.of(1990, 1, 1),
            "(11) 99999-9999",
            "123.456.789-01",
            "joao.silva@example.com"
        );

        when(responsavelRepo.findById(responsavelId)).thenReturn(java.util.Optional.of(responsavel));
        when(alunoRepo.findAllByResponsavelId(responsavelId)).thenReturn(List.of());

        service.deleteResponsavel(responsavelId, false);

        verify(responsavelRepo).delete(responsavel);
    }

    @Test
    void shouldRejectDeleteWhenResponsavelHasStudentsLinked() {
        var responsavelId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var responsavel = new Responsavel(
            "João Silva",
            LocalDate.of(1990, 1, 1),
            "(11) 99999-9999",
            "123.456.789-01",
            "joao.silva@example.com"
        );

        when(responsavelRepo.findById(responsavelId)).thenReturn(java.util.Optional.of(responsavel));
        when(alunoRepo.findAllByResponsavelId(responsavelId)).thenReturn(List.of(Mockito.mock(Aluno.class)));

        var exception = assertThrows(BusinessException.class, () -> service.deleteResponsavel(responsavelId, false));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(
            "Este responsável possui alunos vinculados. Exclua os alunos antes de excluir o responsável.",
            exception.getMessage()
        );
        verify(responsavelRepo, never()).delete(Mockito.any(Responsavel.class));
    }
}
