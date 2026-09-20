package aprimorar.financeiro.pagamentos_particular.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import aprimorar.financeiro.common.FormaPagamentoEnum;
import aprimorar.financeiro.pagamentos_particular.domain.exception.PagamentoParticularDadosInvalidosException;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PagamentoParticularTest {

    @Test
    void shouldCreatePayment() {
        PagamentoParticular pagamento = new PagamentoParticular(
            LocalDate.now(),
            new BigDecimal("80.00"),
            FormaPagamentoEnum.PIX,
            "https://example.com/comprovante.pdf"
        );

        assertNotNull(pagamento.getId());
        assertEquals(LocalDate.now(), pagamento.getDataPagamento());
        assertEquals(new BigDecimal("80.00"), pagamento.getValorTotal());
        assertEquals(FormaPagamentoEnum.PIX, pagamento.getFormaPagamento());
        assertEquals("https://example.com/comprovante.pdf", pagamento.getComprovanteUrl());
    }

    @Test
    void shouldAllowPaymentWithoutReceipt() {
        PagamentoParticular pagamento = new PagamentoParticular(
            LocalDate.now(),
            new BigDecimal("80.00"),
            FormaPagamentoEnum.PIX,
            null
        );

        assertNull(pagamento.getComprovanteUrl());
    }

    @Test
    void shouldRejectFuturePaymentDate() {
        assertThrows(
            PagamentoParticularDadosInvalidosException.class,
            () -> new PagamentoParticular(
                LocalDate.now().plusDays(1),
                new BigDecimal("80.00"),
                FormaPagamentoEnum.PIX,
                null
            )
        );
    }
}
