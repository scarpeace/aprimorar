package aprimorar.financeiro.recebimentos_alunos.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.recebimentos_alunos.domain.Cobranca;
import aprimorar.financeiro.recebimentos_alunos.domain.CobrancaRecebimento;
import aprimorar.financeiro.recebimentos_alunos.domain.enums.StatusCobranca;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.CobrancaDadosInvalidosException;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.RecebimentoDadosInvalidosException;
import aprimorar.financeiro.recebimentos_alunos.infrastructure.CobrancaRepository;
import aprimorar.financeiro.recebimentos_alunos.infrastructure.RecebimentoRepository;
import aprimorar.financeiro.recebimentos_alunos.web.dto.RegistrarRecebimentoRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecebimentoServiceTest {

    private static final UUID ALUNO_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID OUTRO_ALUNO_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");

    @Mock
    private RecebimentoRepository recebimentoRepository;

    @Mock
    private CobrancaRepository cobrancaRepository;

    private RecebimentoService service;

    @BeforeEach
    void setUp() {
        service = new RecebimentoService(recebimentoRepository, cobrancaRepository);
    }

    @Test
    void deveRegistrarRecebimentoParaCobrancasDoMesmoAluno() {
        Cobranca primeira = cobranca(10L, ALUNO_ID, "80.00");
        Cobranca segunda = cobranca(20L, ALUNO_ID, "120.00");
        when(cobrancaRepository.findAllByIdInForUpdate(List.of(1L, 2L)))
            .thenReturn(List.of(primeira, segunda));
        when(recebimentoRepository.save(any(CobrancaRecebimento.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        UUID recebimentoId = service.registrarRecebimento(
            new RegistrarRecebimentoRequest(
                List.of(1L, 2L),
                LocalDate.now(),
                FormaPagamentoEnum.PIX,
                null
            )
        );

        ArgumentCaptor<CobrancaRecebimento> captor =
            ArgumentCaptor.forClass(CobrancaRecebimento.class);
        verify(recebimentoRepository).save(captor.capture());
        assertEquals(recebimentoId, captor.getValue().getId());
        assertEquals(new BigDecimal("200.00"), captor.getValue().getValorTotal());
        assertEquals(StatusCobranca.PAGA, primeira.getStatus());
        assertEquals(StatusCobranca.PAGA, segunda.getStatus());
    }

    @Test
    void naoDeveRegistrarRecebimentoComCobrancasDeAlunosDiferentes() {
        Cobranca primeira = cobranca(10L, ALUNO_ID, "80.00");
        Cobranca segunda = cobranca(20L, OUTRO_ALUNO_ID, "120.00");
        when(cobrancaRepository.findAllByIdInForUpdate(List.of(1L, 2L)))
            .thenReturn(List.of(primeira, segunda));

        assertThrows(
            RecebimentoDadosInvalidosException.class,
            () -> service.registrarRecebimento(
                new RegistrarRecebimentoRequest(
                    List.of(1L, 2L),
                    LocalDate.now(),
                    FormaPagamentoEnum.PIX,
                    null
                )
            )
        );
    }

    @Test
    void naoDeveRegistrarRecebimentoComCobrancaJaPaga() {
        Cobranca cobranca = cobranca(10L, ALUNO_ID, "80.00");
        cobranca.vincularRecebimento(recebimento());
        when(cobrancaRepository.findAllByIdInForUpdate(List.of(1L)))
            .thenReturn(List.of(cobranca));

        assertThrows(
            CobrancaDadosInvalidosException.class,
            () -> service.registrarRecebimento(
                new RegistrarRecebimentoRequest(
                    List.of(1L),
                    LocalDate.now(),
                    FormaPagamentoEnum.PIX,
                    null
                )
            )
        );
    }

    @Test
    void deveCancelarRecebimentoEDeixarCobrancasPendentes() {
        Cobranca cobranca = cobranca(10L, ALUNO_ID, "80.00");
        CobrancaRecebimento recebimento = recebimento();
        cobranca.vincularRecebimento(recebimento);
        when(recebimentoRepository.findByIdForUpdate(recebimento.getId()))
            .thenReturn(Optional.of(recebimento));
        when(cobrancaRepository.findAllByRecebimentoIdForUpdate(recebimento.getId()))
            .thenReturn(List.of(cobranca));

        service.cancelarRecebimento(recebimento.getId());

        assertEquals(StatusCobranca.PENDENTE, cobranca.getStatus());
        verify(cobrancaRepository).flush();
        verify(recebimentoRepository).delete(recebimento);
    }

    private static Cobranca cobranca(Long atendimentoId, UUID alunoId, String valor) {
        return new Cobranca(atendimentoId, alunoId, new BigDecimal(valor));
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
