package aprimorar.financeiro.recebimentos_alunos.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.financeiro.recebimentos_alunos.api.commands.AtualizarCobrancaCommandApi;
import aprimorar.financeiro.recebimentos_alunos.api.commands.CriarCobrancaCommandApi;
import aprimorar.financeiro.recebimentos_alunos.domain.Cobranca;
import aprimorar.financeiro.recebimentos_alunos.domain.CobrancaRecebimento;
import aprimorar.financeiro.recebimentos_alunos.domain.enums.StatusCobranca;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.CobrancaDadosInvalidosException;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.CobrancaJaExistenteException;
import aprimorar.financeiro.recebimentos_alunos.infrastructure.CobrancaRepository;
import aprimorar.common.FormaPagamentoEnum;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.hibernate.exception.ConstraintViolationException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class RecebimentosApiImplTest {

    private static final UUID ALUNO_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    @Mock
    private CobrancaRepository cobrancaRepository;

    private RecebimentosApiImpl api;

    @BeforeEach
    void setUp() {
        api = new RecebimentosApiImpl(cobrancaRepository);
    }

    @Test
    void deveCriarCobrancaPendente() {
        api.criarCobranca(new CriarCobrancaCommandApi(10L, ALUNO_ID, new BigDecimal("80.00")));

        ArgumentCaptor<Cobranca> captor = ArgumentCaptor.forClass(Cobranca.class);
        verify(cobrancaRepository).saveAndFlush(captor.capture());
        assertEquals(10L, captor.getValue().getAtendimentoId());
        assertEquals(ALUNO_ID, captor.getValue().getAlunoId());
        assertEquals(new BigDecimal("80.00"), captor.getValue().getValor());
        assertEquals(StatusCobranca.PENDENTE, captor.getValue().getStatus());
    }

    @Test
    void deveTraduzirCobrancaJaExistente() {
        when(cobrancaRepository.saveAndFlush(any(Cobranca.class))).thenThrow(
            violacaoDaConstraint("uk_cobrancas_atendimento")
        );

        assertThrows(
            CobrancaJaExistenteException.class,
            () -> api.criarCobranca(
                new CriarCobrancaCommandApi(
                    10L,
                    ALUNO_ID,
                    new BigDecimal("80.00")
                )
            )
        );
    }

    @Test
    void deveAtualizarCobrancaPendente() {
        Cobranca cobranca = cobranca(10L);
        when(cobrancaRepository.findByAtendimentoIdForUpdate(10L))
            .thenReturn(Optional.of(cobranca));

        UUID novoAlunoId = UUID.fromString("44444444-4444-4444-4444-444444444444");
        api.atualizarCobranca(
            new AtualizarCobrancaCommandApi(10L, novoAlunoId, new BigDecimal("90.00"))
        );

        assertEquals(novoAlunoId, cobranca.getAlunoId());
        assertEquals(new BigDecimal("90.00"), cobranca.getValor());
    }

    @Test
    void naoDeveAtualizarCobrancaPaga() {
        Cobranca cobranca = cobranca(10L);
        cobranca.vincularRecebimento(recebimento());
        when(cobrancaRepository.findByAtendimentoIdForUpdate(10L))
            .thenReturn(Optional.of(cobranca));

        assertThrows(
            CobrancaDadosInvalidosException.class,
            () -> api.atualizarCobranca(
                new AtualizarCobrancaCommandApi(10L, ALUNO_ID, new BigDecimal("90.00"))
            )
        );
    }

    @Test
    void deveCancelarCobrancaPendente() {
        Cobranca cobranca = cobranca(10L);
        when(cobrancaRepository.findByAtendimentoIdForUpdate(10L))
            .thenReturn(Optional.of(cobranca));

        api.cancelarCobranca(10L);

        assertEquals(StatusCobranca.CANCELADA, cobranca.getStatus());
    }

    @Test
    void naoDeveCancelarCobrancaPaga() {
        Cobranca cobranca = cobranca(10L);
        cobranca.vincularRecebimento(recebimento());
        when(cobrancaRepository.findByAtendimentoIdForUpdate(10L))
            .thenReturn(Optional.of(cobranca));

        assertThrows(CobrancaDadosInvalidosException.class, () -> api.cancelarCobranca(10L));
    }

    @Test
    void deveConsultarPendenciaPorAluno() {
        when(cobrancaRepository.existsByAlunoIdAndStatusIn(
            ALUNO_ID,
            List.of(StatusCobranca.PENDENTE, StatusCobranca.ATRASADA)
        ))
            .thenReturn(true);

        assertEquals(true, api.possuiCobrancaPendente(ALUNO_ID));
    }

    private static Cobranca cobranca(Long atendimentoId) {
        return new Cobranca(atendimentoId, ALUNO_ID, new BigDecimal("80.00"));
    }

    private static CobrancaRecebimento recebimento() {
        return new CobrancaRecebimento(
            LocalDate.now(),
            new BigDecimal("80.00"),
            FormaPagamentoEnum.PIX,
            null
        );
    }

    private static DataIntegrityViolationException violacaoDaConstraint(
        String constraint
    ) {
        return new DataIntegrityViolationException(
            "violação de constraint",
            new ConstraintViolationException(
                "violação de constraint",
                new SQLException(),
                "",
                constraint
            )
        );
    }
}
