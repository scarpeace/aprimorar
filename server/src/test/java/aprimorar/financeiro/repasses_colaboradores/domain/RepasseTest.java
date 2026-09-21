package aprimorar.financeiro.repasses_colaboradores.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.repasses_colaboradores.domain.RepassePagamento;
import aprimorar.financeiro.repasses_colaboradores.domain.Repasse;
import aprimorar.financeiro.repasses_colaboradores.domain.enums.StatusRepasse;
import aprimorar.financeiro.repasses_colaboradores.domain.exception.RepasseDadosInvalidosException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class RepasseTest {

    private static final UUID COLABORADOR_ID =
        UUID.fromString("33333333-3333-3333-3333-333333333333");

    @Test
    void shouldCreatePendingRepasse() {
        Repasse repasse = repasse();

        assertEquals(10L, repasse.getAtendimentoId());
        assertEquals(COLABORADOR_ID, repasse.getColaboradorId());
        assertEquals(new BigDecimal("80.00"), repasse.getValor());
        assertEquals(StatusRepasse.PENDENTE, repasse.getStatus());
    }

    @Test
    void shouldCalculateOverdueStatus() {
        Repasse repasse = repasse();
        ReflectionTestUtils.setField(
            repasse,
            "createdAt",
            LocalDateTime.now().minusDays(31)
        );

        assertEquals(StatusRepasse.ATRASADO, repasse.statusAtual());
        assertEquals(StatusRepasse.PENDENTE, repasse.getStatus());
    }

    @Test
    void shouldLinkAndUnlinkPayment() {
        Repasse repasse = repasse();
        RepassePagamento pagamento = pagamento();

        repasse.vincularPagamento(pagamento);

        assertSame(pagamento, repasse.getPagamento());
        assertEquals(StatusRepasse.PAGO, repasse.getStatus());

        repasse.desvincularPagamento();

        assertNull(repasse.getPagamento());
        assertEquals(StatusRepasse.PENDENTE, repasse.getStatus());
    }

    @Test
    void shouldUpdatePendingRepasse() {
        Repasse repasse = repasse();
        UUID novoColaboradorId =
            UUID.fromString("44444444-4444-4444-4444-444444444444");

        repasse.atualizar(novoColaboradorId, new BigDecimal("90.00"));

        assertEquals(novoColaboradorId, repasse.getColaboradorId());
        assertEquals(new BigDecimal("90.00"), repasse.getValor());
    }

    @Test
    void shouldNotUpdatePaidRepasse() {
        Repasse repasse = repasse();
        repasse.vincularPagamento(pagamento());

        assertThrows(
            RepasseDadosInvalidosException.class,
            () -> repasse.atualizar(COLABORADOR_ID, new BigDecimal("90.00"))
        );
    }

    @Test
    void shouldCancelPendingRepasse() {
        Repasse repasse = repasse();

        repasse.cancelar();

        assertEquals(StatusRepasse.CANCELADO, repasse.getStatus());
    }

    @Test
    void shouldNotCancelPaidRepasse() {
        Repasse repasse = repasse();
        repasse.vincularPagamento(pagamento());

        assertThrows(
            RepasseDadosInvalidosException.class,
            repasse::cancelar
        );
    }

    private static Repasse repasse() {
        return new Repasse(
            10L,
            COLABORADOR_ID,
            new BigDecimal("80.00")
        );
    }

    private static RepassePagamento pagamento() {
        return new RepassePagamento(
            LocalDate.now(),
            new BigDecimal("80.00"),
            FormaPagamentoEnum.PIX,
            null
        );
    }
}
