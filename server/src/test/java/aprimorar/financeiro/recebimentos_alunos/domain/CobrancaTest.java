package aprimorar.financeiro.recebimentos_alunos.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.recebimentos_alunos.domain.enums.StatusCobranca;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.CobrancaDadosInvalidosException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CobrancaTest {

    private static final UUID ALUNO_ID =
        UUID.fromString("33333333-3333-3333-3333-333333333333");

    @Test
    void deveCriarCobrancaPendente() {
        Cobranca cobranca = cobranca();

        assertEquals(10L, cobranca.getAtendimentoId());
        assertEquals(ALUNO_ID, cobranca.getAlunoId());
        assertEquals(new BigDecimal("80.00"), cobranca.getValor());
        assertEquals(StatusCobranca.PENDENTE, cobranca.getStatus());
    }

    @Test
    void deveVincularEDesvincularRecebimento() {
        Cobranca cobranca = cobranca();
        CobrancaRecebimento recebimento = recebimento();

        cobranca.vincularRecebimento(recebimento);

        assertSame(recebimento, cobranca.getRecebimento());
        assertEquals(StatusCobranca.PAGA, cobranca.getStatus());

        cobranca.desvincularRecebimento();

        assertNull(cobranca.getRecebimento());
        assertEquals(StatusCobranca.PENDENTE, cobranca.getStatus());
    }

    @Test
    void deveAtualizarCobrancaPendente() {
        Cobranca cobranca = cobranca();
        UUID novoAlunoId = UUID.fromString("44444444-4444-4444-4444-444444444444");

        cobranca.atualizar(novoAlunoId, new BigDecimal("90.00"));

        assertEquals(novoAlunoId, cobranca.getAlunoId());
        assertEquals(new BigDecimal("90.00"), cobranca.getValor());
    }

    @Test
    void naoDeveAtualizarCobrancaPaga() {
        Cobranca cobranca = cobranca();
        cobranca.vincularRecebimento(recebimento());

        assertThrows(
            CobrancaDadosInvalidosException.class,
            () -> cobranca.atualizar(ALUNO_ID, new BigDecimal("90.00"))
        );
    }

    @Test
    void deveCancelarCobrancaPendente() {
        Cobranca cobranca = cobranca();

        cobranca.cancelar();

        assertEquals(StatusCobranca.CANCELADA, cobranca.getStatus());
    }

    @Test
    void naoDeveCancelarCobrancaPaga() {
        Cobranca cobranca = cobranca();
        cobranca.vincularRecebimento(recebimento());

        assertThrows(CobrancaDadosInvalidosException.class, cobranca::cancelar);
    }

    private static Cobranca cobranca() {
        return new Cobranca(10L, ALUNO_ID, new BigDecimal("80.00"));
    }

    private static CobrancaRecebimento recebimento() {
        return new CobrancaRecebimento(
            LocalDate.now(),
            new BigDecimal("80.00"),
            FormaPagamentoEnum.PIX,
            null
        );
    }
}
