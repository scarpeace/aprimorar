package aprimorar.agendamento.colaboradores.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import aprimorar.agendamento.colaboradores.domain.enums.FuncoesColaborador;
import aprimorar.agendamento.colaboradores.domain.exception.ColaboradorPossuiRepassePendenteException;
import aprimorar.agendamento.colaboradores.infrastructure.ColaboradorRepository;

import aprimorar.agendamento.common.domain.Endereco;
import aprimorar.financeiro.repasses_colaboradores.api.PagamentosApi;
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
    private PagamentosApi repasseApi;

    private ColaboradorService service;

    @BeforeEach
    void setUp() {
        service = new ColaboradorService(colaboradorRepo, repasseApi);
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
        when(repasseApi.possuiRepassePendente(id)).thenReturn(true);

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
