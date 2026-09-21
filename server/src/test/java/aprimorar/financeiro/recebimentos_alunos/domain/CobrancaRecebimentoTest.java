package aprimorar.financeiro.recebimentos_alunos.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.RecebimentoDadosInvalidosException;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CobrancaRecebimentoTest {

    @Test
    void deveCriarRecebimento() {
        CobrancaRecebimento recebimento = new CobrancaRecebimento(
            LocalDate.now(),
            new BigDecimal("80.00"),
            FormaPagamentoEnum.PIX,
            "https://example.com/comprovante.pdf"
        );

        assertNotNull(recebimento.getId());
        assertEquals(LocalDate.now(), recebimento.getDataRecebimento());
        assertEquals(new BigDecimal("80.00"), recebimento.getValorTotal());
        assertEquals(FormaPagamentoEnum.PIX, recebimento.getFormaPagamento());
        assertEquals("https://example.com/comprovante.pdf", recebimento.getComprovanteUrl());
    }

    @Test
    void devePermitirRecebimentoSemComprovante() {
        CobrancaRecebimento recebimento = new CobrancaRecebimento(
            LocalDate.now(),
            new BigDecimal("80.00"),
            FormaPagamentoEnum.PIX,
            null
        );

        assertNull(recebimento.getComprovanteUrl());
    }

    @Test
    void deveRejeitarDataFutura() {
        assertThrows(
            RecebimentoDadosInvalidosException.class,
            () -> new CobrancaRecebimento(
                LocalDate.now().plusDays(1),
                new BigDecimal("80.00"),
                FormaPagamentoEnum.PIX,
                null
            )
        );
    }
}
