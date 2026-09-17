package aprimorar.financeiro.cobrancas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.financeiro.api.cobrancas.AtualizarCobrancaCommand;
import aprimorar.financeiro.api.cobrancas.CriarCobrancaCommand;
import aprimorar.financeiro.api.cobrancas.CobrancaResumo;
import aprimorar.financeiro.cobrancas.domain.CobrancaIndividualEntity;
import aprimorar.financeiro.cobrancas.domain.enums.StatusCobrancaIndividual;
import aprimorar.financeiro.cobrancas.domain.exception.CobrancaIndividualDadosInvalidosException;
import aprimorar.financeiro.cobrancas.repository.CobrancaIndividualRepository;
import aprimorar.financeiro.common.FormaPagamentoEnum;
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
class CobrancaIndividualServiceTest {

    private static final UUID ALUNO_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Mock
    private CobrancaIndividualRepository cobrancaRepository;

    private CobrancaIndividualService service;

    @BeforeEach
    void setUp() {
        service = new CobrancaIndividualService(cobrancaRepository);
    }

    @Test
    void deveCriarCobrancaPendente() {
        when(cobrancaRepository.existsByAtendimentoId(10L)).thenReturn(false);

        service.criar(new CriarCobrancaCommand(10L, ALUNO_ID, new BigDecimal("100.00")));

        ArgumentCaptor<CobrancaIndividualEntity> captor = ArgumentCaptor.forClass(CobrancaIndividualEntity.class);
        verify(cobrancaRepository).save(captor.capture());
        assertEquals(10L, captor.getValue().getAtendimentoId());
        assertEquals(ALUNO_ID, captor.getValue().getAlunoId());
        assertEquals(new BigDecimal("100.00"), captor.getValue().getValor());
        assertEquals(StatusCobrancaIndividual.PENDENTE, captor.getValue().getStatus());
    }

    @Test
    void naoDeveCriarCobrancaDuplicadaParaAtendimento() {
        when(cobrancaRepository.existsByAtendimentoId(10L)).thenReturn(true);

        assertThrows(
            CobrancaIndividualDadosInvalidosException.class,
            () -> service.criar(new CriarCobrancaCommand(10L, ALUNO_ID, new BigDecimal("100.00")))
        );

        verify(cobrancaRepository, never()).save(any(CobrancaIndividualEntity.class));
    }

    @Test
    void deveAtualizarCobrancaPendente() {
        CobrancaIndividualEntity cobranca = cobranca(10L);
        when(cobrancaRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(Optional.of(cobranca));

        UUID novoAlunoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        service.atualizar(new AtualizarCobrancaCommand(10L, novoAlunoId, new BigDecimal("150.00")));

        assertEquals(novoAlunoId, cobranca.getAlunoId());
        assertEquals(new BigDecimal("150.00"), cobranca.getValor());
    }

    @Test
    void naoDeveAtualizarCobrancaPaga() {
        CobrancaIndividualEntity cobranca = cobranca(10L);
        cobranca.registrarPagamento(UUID.randomUUID(), FormaPagamentoEnum.PIX, null);
        when(cobrancaRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(Optional.of(cobranca));

        assertThrows(
            CobrancaIndividualDadosInvalidosException.class,
            () -> service.atualizar(new AtualizarCobrancaCommand(10L, ALUNO_ID, new BigDecimal("150.00")))
        );
    }

    @Test
    void deveCancelarCobrancaPendente() {
        CobrancaIndividualEntity cobranca = cobranca(10L);
        when(cobrancaRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(Optional.of(cobranca));

        service.cancelarPorAtendimento(10L);

        assertEquals(StatusCobrancaIndividual.CANCELADO, cobranca.getStatus());
    }

    @Test
    void naoDeveCancelarCobrancaPaga() {
        CobrancaIndividualEntity cobranca = cobranca(10L);
        cobranca.registrarPagamento(UUID.randomUUID(), FormaPagamentoEnum.PIX, null);
        when(cobrancaRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(Optional.of(cobranca));

        assertThrows(CobrancaIndividualDadosInvalidosException.class, () -> service.cancelarPorAtendimento(10L));
    }

    @Test
    void deveConsultarPendenciaPorAluno() {
        when(cobrancaRepository.existsByAlunoIdAndStatus(ALUNO_ID, StatusCobrancaIndividual.PENDENTE))
            .thenReturn(true);

        boolean possuiPendencia = service.possuiPendenciaPorAlunoId(ALUNO_ID);

        assertEquals(true, possuiPendencia);
    }

    @Test
    void deveBuscarResumosEmLote() {
        CobrancaIndividualEntity primeira = cobranca(10L);
        CobrancaIndividualEntity segunda = cobranca(20L);
        when(cobrancaRepository.findAllByAtendimentoIdIn(Set.of(10L, 20L)))
            .thenReturn(List.of(primeira, segunda));

        Map<Long, CobrancaResumo> resumos = service.buscarResumosPorAtendimentoIds(Set.of(10L, 20L));

        assertEquals(2, resumos.size());
        assertEquals(new BigDecimal("100.00"), resumos.get(10L).valor());
        assertEquals(StatusCobrancaIndividual.PENDENTE.name(), resumos.get(20L).status());
    }

    @Test
    void naoDeveConsultarRepositoryParaLoteVazio() {
        Map<Long, CobrancaResumo> resumos = service.buscarResumosPorAtendimentoIds(Set.of());

        assertEquals(Map.of(), resumos);
        verify(cobrancaRepository, never()).findAllByAtendimentoIdIn(any());
    }

    private static CobrancaIndividualEntity cobranca(Long atendimentoId) {
        return new CobrancaIndividualEntity(atendimentoId, ALUNO_ID, new BigDecimal("100.00"));
    }
}
