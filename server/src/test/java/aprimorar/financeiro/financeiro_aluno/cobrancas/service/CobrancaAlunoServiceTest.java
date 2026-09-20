package aprimorar.financeiro.financeiro_aluno.cobrancas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.financeiro.api.financeiro_aluno.AtualizarCobrancaAlunoCommand;
import aprimorar.financeiro.api.financeiro_aluno.CobrancaAlunoResumo;
import aprimorar.financeiro.api.financeiro_aluno.CriarCobrancaAlunoCommand;
import aprimorar.financeiro.api.financeiro_aluno.TipoOrigemCobrancaAluno;
import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.CobrancaAluno;
import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.enums.StatusCobrancaAluno;
import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.exception.CobrancaAlunoDadosInvalidosException;
import aprimorar.financeiro.financeiro_aluno.cobrancas.repository.CobrancaAlunoRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CobrancaAlunoServiceTest {

    private static final UUID ALUNO_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Mock
    private CobrancaAlunoRepository cobrancaRepository;

    private CobrancaAlunoService service;

    @BeforeEach
    void setUp() {
        service = new CobrancaAlunoService(cobrancaRepository);
    }

    @Test
    void deveCriarCobrancaPendenteComParcelaUnica() {
        when(cobrancaRepository.existsByOrigemIdAndOrigemTipo(10L, TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL))
            .thenReturn(false);

        service.criar(new CriarCobrancaAlunoCommand(
            10L,
            TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL,
            ALUNO_ID,
            new BigDecimal("100.00")
        ));

        ArgumentCaptor<CobrancaAluno> captor = ArgumentCaptor.forClass(CobrancaAluno.class);
        verify(cobrancaRepository).save(captor.capture());
        CobrancaAluno cobranca = captor.getValue();

        assertEquals(10L, cobranca.getOrigemId());
        assertEquals(TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL, cobranca.getOrigemTipo());
        assertEquals(ALUNO_ID, cobranca.getAlunoId());
        assertEquals(new BigDecimal("100.00"), cobranca.getValorTotal());
        assertEquals(StatusCobrancaAluno.PENDENTE, cobranca.getStatus());
        assertEquals(1, cobranca.getParcelas().size());
        assertEquals(new BigDecimal("100.00"), cobranca.getParcelas().getFirst().getValor());
    }

    @Test
    void naoDeveCriarCobrancaDuplicadaParaOrigem() {
        when(cobrancaRepository.existsByOrigemIdAndOrigemTipo(10L, TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL))
            .thenReturn(true);

        assertThrows(
            CobrancaAlunoDadosInvalidosException.class,
            () -> service.criar(new CriarCobrancaAlunoCommand(
                10L,
                TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL,
                ALUNO_ID,
                new BigDecimal("100.00")
            ))
        );

        verify(cobrancaRepository, never()).save(any(CobrancaAluno.class));
    }

    @Test
    void deveAtualizarCobrancaPendenteEParcelaUnica() {
        CobrancaAluno cobranca = cobranca(10L);
        when(cobrancaRepository.findByOrigemIdAndOrigemTipoForUpdate(
            10L,
            TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL
        )).thenReturn(Optional.of(cobranca));

        UUID novoAlunoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        service.atualizar(new AtualizarCobrancaAlunoCommand(
            10L,
            TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL,
            novoAlunoId,
            new BigDecimal("150.00")
        ));

        assertEquals(novoAlunoId, cobranca.getAlunoId());
        assertEquals(new BigDecimal("150.00"), cobranca.getValorTotal());
        assertEquals(new BigDecimal("150.00"), cobranca.getParcelas().getFirst().getValor());
    }

    @Test
    void naoDeveAtualizarCobrancaCancelada() {
        CobrancaAluno cobranca = cobranca(10L);
        cobranca.cancelar();
        when(cobrancaRepository.findByOrigemIdAndOrigemTipoForUpdate(
            10L,
            TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL
        )).thenReturn(Optional.of(cobranca));

        assertThrows(
            CobrancaAlunoDadosInvalidosException.class,
            () -> service.atualizar(new AtualizarCobrancaAlunoCommand(
                10L,
                TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL,
                ALUNO_ID,
                new BigDecimal("150.00")
            ))
        );
    }

    @Test
    void deveCancelarCobrancaEParcela() {
        CobrancaAluno cobranca = cobranca(10L);
        when(cobrancaRepository.findByOrigemIdAndOrigemTipoForUpdate(
            10L,
            TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL
        )).thenReturn(Optional.of(cobranca));

        service.cancelarPorOrigem(10L, TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL);

        assertEquals(StatusCobrancaAluno.CANCELADA, cobranca.getStatus());
        assertEquals(
            aprimorar.financeiro.financeiro_aluno.parcelas.domain.enums.StatusParcelaAluno.CANCELADA,
            cobranca.getParcelas().getFirst().getStatus()
        );
    }

    @Test
    void naoDeveCancelarCobrancaJaCancelada() {
        CobrancaAluno cobranca = cobranca(10L);
        cobranca.cancelar();
        when(cobrancaRepository.findByOrigemIdAndOrigemTipoForUpdate(
            10L,
            TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL
        )).thenReturn(Optional.of(cobranca));

        assertThrows(
            CobrancaAlunoDadosInvalidosException.class,
            () -> service.cancelarPorOrigem(10L, TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL)
        );
    }

    @Test
    void deveConsultarPendenciaPorAluno() {
        List<StatusCobrancaAluno> statuses = List.of(
            StatusCobrancaAluno.PENDENTE,
            StatusCobrancaAluno.PARCIALMENTE_PAGA
        );
        when(cobrancaRepository.existsByAlunoIdAndStatusIn(ALUNO_ID, statuses)).thenReturn(true);

        boolean possuiPendencia = service.possuiPendenciaPorAlunoId(ALUNO_ID);

        assertEquals(true, possuiPendencia);
    }

    @Test
    void deveBuscarResumosPorOrigem() {
        CobrancaAluno primeira = cobranca(10L);
        CobrancaAluno segunda = cobranca(20L);
        when(cobrancaRepository.findAllByOrigemIdInAndOrigemTipo(
            Set.of(10L, 20L),
            TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL
        )).thenReturn(List.of(primeira, segunda));

        Map<Long, CobrancaAlunoResumo> resumos = service.buscarResumosPorOrigemIds(
            Set.of(10L, 20L),
            TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL
        );

        assertEquals(2, resumos.size());
        assertEquals(new BigDecimal("100.00"), resumos.get(10L).valor());
        assertEquals(StatusCobrancaAluno.PENDENTE.name(), resumos.get(20L).status());
    }

    @Test
    void naoDeveConsultarRepositoryQuandoNaoHaOrigens() {
        Map<Long, CobrancaAlunoResumo> resumos = service.buscarResumosPorOrigemIds(
            Set.of(),
            TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL
        );

        assertEquals(Map.of(), resumos);
        verify(cobrancaRepository, never()).findAllByOrigemIdInAndOrigemTipo(any(), any());
    }

    private static CobrancaAluno cobranca(Long origemId) {
        return new CobrancaAluno(
            origemId,
            TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL,
            ALUNO_ID,
            new BigDecimal("100.00")
        );
    }
}
