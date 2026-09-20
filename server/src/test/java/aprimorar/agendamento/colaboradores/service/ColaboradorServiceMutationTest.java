package aprimorar.agendamento.colaboradores.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.financeiro.api.repasses_particular.RepasseApi;
import aprimorar.agendamento.colaboradores.domain.enums.FuncoesColaborador;
import aprimorar.agendamento.colaboradores.domain.exception.ColaboradorDuplicadoException;
import aprimorar.agendamento.colaboradores.domain.exception.ColaboradorPossuiRepassePendenteException;
import aprimorar.agendamento.colaboradores.repository.ColaboradorRepository;

import aprimorar.agendamento.colaboradores.web.dto.colaborador.ColaboradorRequest;
import aprimorar.agendamento.common.domain.Endereco;
import aprimorar.agendamento.common.web.dto.endereco.EnderecoRequest;
import aprimorar.agendamento.colaboradores.domain.Colaborador;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ColaboradorServiceMutationTest {

    @Mock
    private ColaboradorRepository colaboradorRepo;

    @Mock
    private RepasseApi repasseApi;

    private ColaboradorService service;

    @BeforeEach
    void setUp() {
        service = new ColaboradorService(colaboradorRepo, repasseApi);
    }

    @Test
    void shouldThrowWhenCreateAndCpfAlreadyExists() {
        var dto = collaboratorRequest();

        when(colaboradorRepo.existsByCpf("12345678900")).thenReturn(true);

        var ex = assertThrows(ColaboradorDuplicadoException.class, () -> service.createColaborador(dto));

        assertEquals("Já existe um colaborador cadastrado com este CPF.", ex.getMessage());
        verify(colaboradorRepo, never()).save(any());
    }

    @Test
    void shouldThrowWhenCreateAndEmailAlreadyExists() {
        var dto = collaboratorRequest();

        when(colaboradorRepo.existsByCpf("12345678900")).thenReturn(false);
        when(colaboradorRepo.existsByEmail("joao@example.com")).thenReturn(true);

        var ex = assertThrows(ColaboradorDuplicadoException.class, () -> service.createColaborador(dto));

        assertEquals("Já existe um colaborador cadastrado com este e-mail.", ex.getMessage());
        verify(colaboradorRepo, never()).save(any());
    }

    @Test
    void shouldThrowWhenUpdateAndEmailAlreadyUsed() {
        UUID id = UUID.randomUUID();
        var colaborador = collaborator();
        setId(colaborador, id);

        when(colaboradorRepo.findById(id)).thenReturn(Optional.of(colaborador));
        when(colaboradorRepo.existsByEmailAndIdNot("joao@example.com", id)).thenReturn(true);

        var request = collaboratorRequest();
        var ex = assertThrows(ColaboradorDuplicadoException.class, () -> service.updateColaborador(id, request));

        assertEquals("Já existe um colaborador utilizando este e-mail.", ex.getMessage());
    }

    @Test
    void shouldDeactivateColaborador() {
        UUID id = UUID.randomUUID();
        var colaborador = collaborator();
        setId(colaborador, id);

        when(colaboradorRepo.findById(id)).thenReturn(Optional.of(colaborador));
        service.deactivateColaborador(id);

        assertFalse(colaborador.getActive());
    }

    @Test
    void shouldNotDeactivateColaboradorWithPendingRepasse() {
        UUID id = UUID.randomUUID();
        var colaborador = collaborator();
        setId(colaborador, id);

        when(colaboradorRepo.findById(id)).thenReturn(Optional.of(colaborador));
        when(repasseApi.possuiPendenciaPorColaboradorId(id)).thenReturn(true);

        var exception = assertThrows(
            ColaboradorPossuiRepassePendenteException.class,
            () -> service.deactivateColaborador(id)
        );

        assertEquals("Não é possível desativar um colaborador com repasse pendente", exception.getMessage());
        assertTrue(colaborador.getActive());
    }

    @Test
    void shouldActivateColaborador() {
        UUID id = UUID.randomUUID();
        var colaborador = collaborator();
        setId(colaborador, id);
        colaborador.deactivate();

        when(colaboradorRepo.findById(id)).thenReturn(Optional.of(colaborador));

        service.activateColaborador(id);

        assertTrue(colaborador.getActive());
    }

    private static ColaboradorRequest collaboratorRequest() {
        return new ColaboradorRequest(
            "João Pereira",
            LocalDate.of(1990, 5, 21),
            "joao@example.com",
            "(61) 99999-9999",
            "123.456.789-00",
            "joao@example.com",
            FuncoesColaborador.PROFESSOR,
            new EnderecoRequest("Rua A", "10", "Apto 1", "Centro", "Brasilia", "DF", "70000000")
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
