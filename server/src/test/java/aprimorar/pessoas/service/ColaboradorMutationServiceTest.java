package aprimorar.pessoas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.atendimentos.repository.AtendimentoRepository;
import aprimorar.exception.BusinessException;
import aprimorar.pessoas.domain.Colaborador;
import aprimorar.pessoas.domain.Endereco;
import aprimorar.pessoas.dto.EnderecoRequestDTO;
import aprimorar.pessoas.dto.colaborador.ColaboradorRequestDTO;
import aprimorar.pessoas.repository.ColaboradorRepository;
import aprimorar.pessoas.shared.FuncoesColaborador;
import java.time.LocalDate;
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
class ColaboradorMutationServiceTest {

    private static final UUID GHOST_ID = UUID.fromString("00000000-0000-4000-8000-000000000001");

    @Mock
    private ColaboradorRepository colaboradorRepo;

    @Mock
    private AtendimentoRepository atendimentoRepo;

    private ColaboradorMutationService service;

    @BeforeEach
    void setUp() {
        service = new ColaboradorMutationService(colaboradorRepo, atendimentoRepo, GHOST_ID.toString());
    }

    @Test
    void shouldCreateColaborador() {
        var dto = collaboratorRequest();
        var saved = dto.toEntity();
        setId(saved, UUID.randomUUID());

        when(colaboradorRepo.existsByCpf("12345678900")).thenReturn(false);
        when(colaboradorRepo.existsByEmail("joao@example.com")).thenReturn(false);
        when(colaboradorRepo.save(any(Colaborador.class))).thenReturn(saved);

        var response = service.createColaborador(dto);

        assertEquals(saved.getId(), response.id());
        assertEquals("João Pereira", response.nome());

        ArgumentCaptor<Colaborador> captor = ArgumentCaptor.forClass(Colaborador.class);
        verify(colaboradorRepo).save(captor.capture());
        assertEquals("12345678900", captor.getValue().getCpf());
        assertEquals("joao@example.com", captor.getValue().getEmail());
        assertEquals("61999999999", captor.getValue().getTelefone());
    }

    @Test
    void shouldThrowWhenCreateAndCpfAlreadyExists() {
        var dto = collaboratorRequest();

        when(colaboradorRepo.existsByCpf("12345678900")).thenReturn(true);

        var ex = assertThrows(BusinessException.class, () -> service.createColaborador(dto));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("Já existe um colaborador cadastrado com este CPF.", ex.getMessage());
        verify(colaboradorRepo, never()).save(any());
    }

    @Test
    void shouldThrowWhenCreateAndEmailAlreadyExists() {
        var dto = collaboratorRequest();

        when(colaboradorRepo.existsByCpf("12345678900")).thenReturn(false);
        when(colaboradorRepo.existsByEmail("joao@example.com")).thenReturn(true);

        var ex = assertThrows(BusinessException.class, () -> service.createColaborador(dto));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("Já existe um colaborador cadastrado com este e-mail.", ex.getMessage());
        verify(colaboradorRepo, never()).save(any());
    }

    @Test
    void shouldUpdateColaborador() {
        UUID id = UUID.randomUUID();
        var colaborador = collaborator();
        setId(colaborador, id);

        var dto = new ColaboradorRequestDTO(
            "Maria Souza",
            LocalDate.of(1992, 8, 10),
            "maria@example.com",
            "(61) 98888-7777",
            "123.456.789-00",
            "maria@example.com",
            FuncoesColaborador.ADMINISTRATIVO,
            new EnderecoRequestDTO("Rua B", "20", "Sala 2", "Centro", "Brasilia", "DF", "70000111")
        );

        when(colaboradorRepo.findById(id)).thenReturn(Optional.of(colaborador));
        when(colaboradorRepo.existsByEmailAndIdNot("maria@example.com", id)).thenReturn(false);

        var response = service.updateColaborador(id, dto);

        assertEquals("Maria Souza", colaborador.getNome());
        assertEquals("maria@example.com", colaborador.getEmail());
        assertEquals("61988887777", colaborador.getTelefone());
        assertEquals(FuncoesColaborador.ADMINISTRATIVO, colaborador.getFuncao());
        assertEquals("Maria Souza", response.nome());
    }

    @Test
    void shouldThrowWhenUpdateGhost() {
        var ghost = collaborator();
        setId(ghost, GHOST_ID);

        when(colaboradorRepo.findById(GHOST_ID)).thenReturn(Optional.of(ghost));

        var ex = assertThrows(BusinessException.class, () -> service.updateColaborador(GHOST_ID, collaboratorRequest()));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("Não é possível modificar o registro de sistema 'Colaborador Removido'.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenUpdateAndEmailAlreadyUsed() {
        UUID id = UUID.randomUUID();
        var colaborador = collaborator();
        setId(colaborador, id);

        when(colaboradorRepo.findById(id)).thenReturn(Optional.of(colaborador));
        when(colaboradorRepo.existsByEmailAndIdNot("joao@example.com", id)).thenReturn(true);

        var ex = assertThrows(BusinessException.class, () -> service.updateColaborador(id, collaboratorRequest()));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("Já existe um colaborador utilizando este e-mail.", ex.getMessage());
    }

    @Test
    void shouldArchiveColaborador() {
        UUID id = UUID.randomUUID();
        var colaborador = collaborator();
        setId(colaborador, id);

        when(colaboradorRepo.findById(id)).thenReturn(Optional.of(colaborador));
        when(atendimentoRepo.colaboradorPossuiAtendimentoAgendado(id)).thenReturn(false);
        when(atendimentoRepo.colaboradorPossuiRepassePendente(id)).thenReturn(false);

        service.archiveColaborador(id);

        assertFalse(colaborador.getActive());
    }

    @Test
    void shouldThrowWhenArchiveAndHasScheduledAtendimento() {
        UUID id = UUID.randomUUID();
        var colaborador = collaborator();
        setId(colaborador, id);

        when(colaboradorRepo.findById(id)).thenReturn(Optional.of(colaborador));
        when(atendimentoRepo.colaboradorPossuiAtendimentoAgendado(id)).thenReturn(true);

        var ex = assertThrows(BusinessException.class, () -> service.archiveColaborador(id));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("Não é possível arquivar um colaborador com atendimentos agendados.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenArchiveAndHasPendingRepasse() {
        UUID id = UUID.randomUUID();
        var colaborador = collaborator();
        setId(colaborador, id);

        when(colaboradorRepo.findById(id)).thenReturn(Optional.of(colaborador));
        when(atendimentoRepo.colaboradorPossuiAtendimentoAgendado(id)).thenReturn(false);
        when(atendimentoRepo.colaboradorPossuiRepassePendente(id)).thenReturn(true);

        var ex = assertThrows(BusinessException.class, () -> service.archiveColaborador(id));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("Não é possível arquivar um colaborador com repasse pendente.", ex.getMessage());
    }

    @Test
    void shouldUnarchiveColaborador() {
        UUID id = UUID.randomUUID();
        var colaborador = collaborator();
        setId(colaborador, id);
        colaborador.archive();

        when(colaboradorRepo.findById(id)).thenReturn(Optional.of(colaborador));

        service.unarchiveColaborador(id);

        assertTrue(colaborador.getActive());
    }

    @Test
    void shouldDeleteColaborador() {
        UUID id = UUID.randomUUID();
        var colaborador = collaborator();
        var ghost = collaborator();

        setId(colaborador, id);
        setId(ghost, GHOST_ID);
        colaborador.archive();

        when(colaboradorRepo.findById(id)).thenReturn(Optional.of(colaborador));
        when(atendimentoRepo.colaboradorPossuiAtendimentoAgendado(id)).thenReturn(false);
        when(atendimentoRepo.colaboradorPossuiRepassePendente(id)).thenReturn(false);
        when(colaboradorRepo.getReferenceById(GHOST_ID)).thenReturn(ghost);

        service.deleteColaborador(id);

        verify(atendimentoRepo).reassignAtendimentosColaboradorToGhost(id, ghost);
        verify(colaboradorRepo).delete(colaborador);
    }

    @Test
    void shouldThrowWhenDeleteAndColaboradorIsNotArchived() {
        UUID id = UUID.randomUUID();
        var colaborador = collaborator();
        setId(colaborador, id);

        when(colaboradorRepo.findById(id)).thenReturn(Optional.of(colaborador));

        var ex = assertThrows(BusinessException.class, () -> service.deleteColaborador(id));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("O colaborador precisa estar arquivado antes de ser excluído.", ex.getMessage());
        verify(colaboradorRepo, never()).delete(any(Colaborador.class));
    }

    @Test
    void shouldThrowWhenDeleteGhost() {
        var ghost = collaborator();
        setId(ghost, GHOST_ID);

        when(colaboradorRepo.findById(GHOST_ID)).thenReturn(Optional.of(ghost));

        var ex = assertThrows(BusinessException.class, () -> service.deleteColaborador(GHOST_ID));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("O registro não pode ser modificado.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenDeleteAndHasBusinessRestriction() {
        UUID id = UUID.randomUUID();
        var colaborador = collaborator();
        setId(colaborador, id);
        colaborador.archive();

        when(colaboradorRepo.findById(id)).thenReturn(Optional.of(colaborador));
        when(atendimentoRepo.colaboradorPossuiAtendimentoAgendado(id)).thenReturn(true);

        var ex = assertThrows(BusinessException.class, () -> service.deleteColaborador(id));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("Não é possível arquivar um colaborador com atendimentos agendados.", ex.getMessage());
        verify(colaboradorRepo, never()).delete(any(Colaborador.class));
    }

    private static ColaboradorRequestDTO collaboratorRequest() {
        return new ColaboradorRequestDTO(
            "João Pereira",
            LocalDate.of(1990, 5, 21),
            "joao@example.com",
            "(61) 99999-9999",
            "123.456.789-00",
            "joao@example.com",
            FuncoesColaborador.PROFESSOR,
            new EnderecoRequestDTO("Rua A", "10", "Apto 1", "Centro", "Brasilia", "DF", "70000000")
        );
    }

    private static Colaborador collaborator() {
        return new Colaborador(
            "João Pereira",
            LocalDate.of(1990, 5, 21),
            "joao@example.com",
            "61999999999",
            "12345678900",
            "joao@example.com",
            FuncoesColaborador.PROFESSOR,
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000000", "Apto 1")
        );
    }

    private static void setId(Colaborador colaborador, UUID id) {
        ReflectionTestUtils.setField(colaborador, "id", id);
    }
}
