package aprimorar.instituicao.atendimentos.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import aprimorar.instituicao.atendimentos.domain.enums.StatusAtendimentoIndividual;
import aprimorar.instituicao.atendimentos.domain.enums.TipoAtendimento;
import aprimorar.instituicao.atendimentos.domain.exception.AtendimentoIndividualDadosInvalidosException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class AtendimentoIndividualEntityTest {

    @Test
    void shouldStartAsScheduled() {
        var atendimento = atendimento();

        assertEquals(StatusAtendimentoIndividual.AGENDADO, atendimento.getStatus());
    }

    @Test
    void shouldMarkScheduledAttendanceAsCompleted() {
        var atendimento = atendimento();

        atendimento.realizar();

        assertEquals(StatusAtendimentoIndividual.REALIZADO, atendimento.getStatus());
    }

    @Test
    void shouldCancelScheduledAttendance() {
        var atendimento = atendimento();

        atendimento.cancelar();

        assertEquals(StatusAtendimentoIndividual.CANCELADO, atendimento.getStatus());
    }

    @Test
    void shouldNotRealizeCancelledAttendance() {
        var atendimento = atendimento();
        atendimento.cancelar();

        var exception = assertThrows(AtendimentoIndividualDadosInvalidosException.class, atendimento::realizar);

        assertEquals("Só é possível realizar um atendimento agendado", exception.getMessage());
    }

    @Test
    void shouldNotCancelCompletedAttendance() {
        var atendimento = atendimento();
        atendimento.realizar();

        var exception = assertThrows(AtendimentoIndividualDadosInvalidosException.class, atendimento::cancelar);

        assertEquals("Só é possível cancelar um atendimento agendado", exception.getMessage());
    }

    @Test
    void shouldNotEditCompletedAttendance() {
        var atendimento = atendimento();
        atendimento.realizar();

        var exception = assertThrows(
            AtendimentoIndividualDadosInvalidosException.class,
            () -> atendimento.update(
                LocalDateTime.of(2026, 9, 17, 10, 0),
                LocalDateTime.of(2026, 9, 17, 11, 0),
                TipoAtendimento.MENTORIA,
                null,
                null
            )
        );

        assertEquals("Só é possível editar um atendimento agendado", exception.getMessage());
    }

    private static AtendimentoIndividualEntity atendimento() {
        return new AtendimentoIndividualEntity(
            LocalDateTime.of(2026, 9, 17, 8, 0),
            LocalDateTime.of(2026, 9, 17, 9, 0),
            TipoAtendimento.AULA,
            null,
            null
        );
    }
}
