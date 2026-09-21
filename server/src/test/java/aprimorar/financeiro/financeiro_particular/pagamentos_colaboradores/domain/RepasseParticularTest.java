package aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.domain.enums.StatusRepasseParticular;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.api.RepasseParticularDadosInvalidosException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class RepasseParticularTest {

    private static final UUID COLABORADOR_ID =
        UUID.fromString("33333333-3333-3333-3333-333333333333");

    @Test
    void shouldCreatePendingRepasse() {
        RepasseParticular repasse = repasse();

        assertEquals(10L, repasse.getAtendimentoId());
        assertEquals(COLABORADOR_ID, repasse.getColaboradorId());
        assertEquals(new BigDecimal("80.00"), repasse.getValor());
        assertEquals(StatusRepasseParticular.PENDENTE, repasse.getStatus());
    }

    @Test
    void shouldCalculateOverdueStatus() {
        RepasseParticular repasse = repasse();
        ReflectionTestUtils.setField(
            repasse,
            "createdAt",
            LocalDateTime.now().minusDays(31)
        );

        assertEquals(StatusRepasseParticular.ATRASADO, repasse.statusAtual());
        assertEquals(StatusRepasseParticular.PENDENTE, repasse.getStatus());
    }

    @Test
    void shouldLinkAndUnlinkPayment() {
        RepasseParticular repasse = repasse();
        PagamentoParticular pagamento = pagamento();

        repasse.vincularPagamento(pagamento);

        assertSame(pagamento, repasse.getPagamento());
        assertEquals(StatusRepasseParticular.PAGO, repasse.getStatus());

        repasse.desvincularPagamento();

        assertNull(repasse.getPagamento());
        assertEquals(StatusRepasseParticular.PENDENTE, repasse.getStatus());
    }

    @Test
    void shouldUpdatePendingRepasse() {
        RepasseParticular repasse = repasse();
        UUID novoColaboradorId =
            UUID.fromString("44444444-4444-4444-4444-444444444444");

        repasse.atualizar(novoColaboradorId, new BigDecimal("90.00"));

        assertEquals(novoColaboradorId, repasse.getColaboradorId());
        assertEquals(new BigDecimal("90.00"), repasse.getValor());
    }

    @Test
    void shouldNotUpdatePaidRepasse() {
        RepasseParticular repasse = repasse();
        repasse.vincularPagamento(pagamento());

        assertThrows(
            RepasseParticularDadosInvalidosException.class,
            () -> repasse.atualizar(COLABORADOR_ID, new BigDecimal("90.00"))
        );
    }

    @Test
    void shouldCancelPendingRepasse() {
        RepasseParticular repasse = repasse();

        repasse.cancelar();

        assertEquals(StatusRepasseParticular.CANCELADO, repasse.getStatus());
    }

    @Test
    void shouldNotCancelPaidRepasse() {
        RepasseParticular repasse = repasse();
        repasse.vincularPagamento(pagamento());

        assertThrows(
            RepasseParticularDadosInvalidosException.class,
            repasse::cancelar
        );
    }

    private static RepasseParticular repasse() {
        return new RepasseParticular(
            10L,
            COLABORADOR_ID,
            new BigDecimal("80.00")
        );
    }

    private static PagamentoParticular pagamento() {
        return new PagamentoParticular(
            LocalDate.now(),
            new BigDecimal("80.00"),
            FormaPagamentoEnum.PIX,
            null
        );
    }
}
