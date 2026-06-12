package aprimorar.pessoas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.pessoas.domain.Colaborador;
import aprimorar.pessoas.domain.Endereco;
import aprimorar.pessoas.dto.ColaboradorRequestDTO;
import aprimorar.pessoas.dto.EnderecoRequestDTO;
import aprimorar.pessoas.dto.ColaboradorResponseDTO;
import aprimorar.pessoas.events.ArchiveColaboradorVerificationEvent;
import aprimorar.pessoas.events.ColaboradorDeletedEvent;
import aprimorar.pessoas.events.DeleteColaboradorVerificationEvent;
import aprimorar.pessoas.repository.ColaboradorRepository;
import aprimorar.pessoas.shared.FuncoesColaborador;
import aprimorar.shared.exception.BusinessException;
import java.time.LocalDate;
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
class ColaboradorMutationServiceTest {

    @Mock
    private ColaboradorRepository colaboradorRepo;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ColaboradorMutationService service;

    @Test
    void shouldCreateColaboradorWhenCpfAndEmailAreAvailable() {
        var dto = validRequest();
        var saved = validColaborador();

        when(colaboradorRepo.existsByCpf("12345678900")).thenReturn(false);
        when(colaboradorRepo.existsByEmail("joao.pereira@example.com")).thenReturn(false);
        when(colaboradorRepo.save(any(Colaborador.class))).thenReturn(saved);

        var response = service.createColaborador(dto);

        assertEquals("João Pereira", response.nome());
        assertEquals("12345678900", response.cpf());
        assertEquals("61999999999", response.telefone());
        assertEquals("joao.pereira@example.com", response.email());
        verify(colaboradorRepo).save(any(Colaborador.class));
    }

    @Test
    void shouldRejectCreateWhenCpfAlreadyExists() {
        when(colaboradorRepo.existsByCpf("12345678900")).thenReturn(true);

        var exception = assertThrows(BusinessException.class, () -> service.createColaborador(validRequest()));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Já existe um colaborador cadastrado com este CPF.", exception.getMessage());
        verify(colaboradorRepo, never()).save(any());
    }

    @Test
    void shouldRejectCreateWhenEmailAlreadyExists() {
        when(colaboradorRepo.existsByCpf("12345678900")).thenReturn(false);
        when(colaboradorRepo.existsByEmail("joao.pereira@example.com")).thenReturn(true);

        var exception = assertThrows(BusinessException.class, () -> service.createColaborador(validRequest()));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Já existe um colaborador cadastrado com este e-mail.", exception.getMessage());
        verify(colaboradorRepo, never()).save(any());
    }

    @Test
    void shouldUpdateColaboradorWhenEmailIsAvailable() {
        var colaboradorId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var existing = validColaborador();
        var dto = new ColaboradorRequestDTO(
            "Maria Pereira",
            LocalDate.of(1992, 6, 10),
            "maria.pereira@example.com",
            "(21) 98888-7777",
            "987.654.321-00",
            "maria.pereira@example.com",
            FuncoesColaborador.ADMINISTRATIVO,
            new EnderecoRequestDTO("Rua B", "20", "Bairro", "Rio", "RJ", "20000-000", "Sala 2")
        );

        when(colaboradorRepo.findById(colaboradorId)).thenReturn(Optional.of(existing));
        when(colaboradorRepo.existsByEmailAndIdNot("maria.pereira@example.com", colaboradorId)).thenReturn(false);

        var response = service.updateColaborador(colaboradorId, dto);

        assertEquals("Maria Pereira", response.nome());
        assertEquals("12345678900", response.cpf());
        assertEquals("21988887777", response.telefone());
        assertEquals(FuncoesColaborador.ADMINISTRATIVO, response.funcao());
        verify(colaboradorRepo, never()).save(any());
    }

    @Test
    void shouldRejectUpdateWhenEmailAlreadyExists() {
        var colaboradorId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var existing = validColaborador();

        when(colaboradorRepo.findById(colaboradorId)).thenReturn(Optional.of(existing));
        when(colaboradorRepo.existsByEmailAndIdNot("maria.pereira@example.com", colaboradorId)).thenReturn(true);

        var exception = assertThrows(BusinessException.class, () -> service.updateColaborador(colaboradorId, validUpdateRequest()));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals("Já existe um colaborador utilizando este e-mail.", exception.getMessage());
    }

    @Test
    void shouldRejectUpdateWhenColaboradorDoesNotExist() {
        var colaboradorId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        when(colaboradorRepo.findById(colaboradorId)).thenReturn(Optional.empty());

        var exception = assertThrows(BusinessException.class, () -> service.updateColaborador(colaboradorId, validUpdateRequest()));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Colaborador não encontrado no banco de dados", exception.getMessage());
    }

    @Test
    void shouldArchiveColaborador() {
        var colaboradorId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var colaborador = validColaborador();

        when(colaboradorRepo.findById(colaboradorId)).thenReturn(Optional.of(colaborador));

        service.archiveColaborador(colaboradorId);

        assertEquals(false, colaborador.getActive());
        verify(eventPublisher).publishEvent(any(ArchiveColaboradorVerificationEvent.class));
    }

    @Test
    void shouldUnarchiveColaborador() {
        var colaboradorId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var colaborador = validColaborador();
        colaborador.archive();

        when(colaboradorRepo.findById(colaboradorId)).thenReturn(Optional.of(colaborador));

        service.unarchiveColaborador(colaboradorId);

        assertEquals(true, colaborador.getActive());
    }

    @Test
    void shouldDeleteColaborador() {
        var colaboradorId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var colaborador = validColaborador();

        when(colaboradorRepo.findById(colaboradorId)).thenReturn(Optional.of(colaborador));

        service.deleteColaborador(colaboradorId);

        verify(eventPublisher).publishEvent(any(DeleteColaboradorVerificationEvent.class));
        verify(colaboradorRepo).delete(colaborador);
        verify(eventPublisher).publishEvent(any(ColaboradorDeletedEvent.class));
    }

    @Test
    void shouldRejectArchiveWhenColaboradorDoesNotExist() {
        var colaboradorId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        when(colaboradorRepo.findById(colaboradorId)).thenReturn(Optional.empty());

        var exception = assertThrows(BusinessException.class, () -> service.archiveColaborador(colaboradorId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    private ColaboradorRequestDTO validRequest() {
        return new ColaboradorRequestDTO(
            "João Pereira",
            LocalDate.of(1990, 5, 21),
            "joao.pereira@example.com",
            "(61) 99999-9999",
            "123.456.789-00",
            "JOAO.PEREIRA@EXAMPLE.COM",
            FuncoesColaborador.PROFESSOR,
            new EnderecoRequestDTO("Rua A", "10", "Centro", "Brasilia", "DF", "70000-000", "Apto 1")
        );
    }

    private ColaboradorRequestDTO validUpdateRequest() {
        return new ColaboradorRequestDTO(
            "Maria Pereira",
            LocalDate.of(1992, 6, 10),
            "maria.pereira@example.com",
            "(21) 98888-7777",
            "987.654.321-00",
            "maria.pereira@example.com",
            FuncoesColaborador.ADMINISTRATIVO,
            new EnderecoRequestDTO("Rua B", "20", "Bairro", "Rio", "RJ", "20000-000", "Sala 2")
        );
    }

    private Colaborador validColaborador() {
        return new Colaborador(
            "João Pereira",
            LocalDate.of(1990, 5, 21),
            "joao.pereira@example.com",
            "(61) 99999-9999",
            "123.456.789-00",
            "JOAO.PEREIRA@EXAMPLE.COM",
            FuncoesColaborador.PROFESSOR,
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000-000", "Apto 1")
        );
    }
}
