package aprimorar.financeiro.repasses_colaboradores.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.repasses_colaboradores.domain.RepassePagamento;
import aprimorar.financeiro.repasses_colaboradores.domain.exception.PagamentoDadosInvalidosException;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class RepassePagamentoTest {

    @Test
    void shouldCreatePayment() {
        RepassePagamento pagamento = new RepassePagamento(
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
        RepassePagamento pagamento = new RepassePagamento(
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
            () -> new RepassePagamento(
                LocalDate.now().plusDays(1),
                new BigDecimal("80.00"),
                FormaPagamentoEnum.PIX,
                null
            )
        );
    }
}
