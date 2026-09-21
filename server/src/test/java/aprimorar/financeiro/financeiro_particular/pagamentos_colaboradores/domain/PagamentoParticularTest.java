package aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.pagamentos_colaboradores.domain.Pagamento;
import aprimorar.financeiro.pagamentos_colaboradores.domain.exception.PagamentoDadosInvalidosException;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PagamentoParticularTest {

    @Test
    void shouldCreatePayment() {
        Pagamento pagamento = new Pagamento(
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
        Pagamento pagamento = new Pagamento(
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
            PagamentoDadosInvalidosException.class,
            () -> new Pagamento(
                LocalDate.now().plusDays(1),
                new BigDecimal("80.00"),
                FormaPagamentoEnum.PIX,
                null
            )
        );
    }
}
