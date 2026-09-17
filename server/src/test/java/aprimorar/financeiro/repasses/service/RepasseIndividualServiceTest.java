package aprimorar.financeiro.repasses.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.financeiro.api.repasses.AtualizarRepasseCommand;
import aprimorar.financeiro.api.repasses.CriarRepasseCommand;
import aprimorar.financeiro.api.repasses.RepasseResumo;
import aprimorar.financeiro.common.FormaPagamentoEnum;
import aprimorar.financeiro.repasses.domain.RepasseIndividualEntity;
import aprimorar.financeiro.repasses.domain.enums.StatusRepasseIndividual;
import aprimorar.financeiro.repasses.domain.exception.RepasseIndividualDadosInvalidosException;
import aprimorar.financeiro.repasses.repository.RepasseIndividualRepository;
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
class RepasseIndividualServiceTest {

    private static final UUID COLABORADOR_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    @Mock
    private RepasseIndividualRepository repasseRepository;

    private RepasseIndividualService service;

    @BeforeEach
    void setUp() {
        service = new RepasseIndividualService(repasseRepository);
    }

    @Test
    void deveCriarRepassePendente() {
        when(repasseRepository.existsByAtendimentoId(10L)).thenReturn(false);

        service.criar(new CriarRepasseCommand(10L, COLABORADOR_ID, new BigDecimal("80.00")));

        ArgumentCaptor<RepasseIndividualEntity> captor = ArgumentCaptor.forClass(RepasseIndividualEntity.class);
        verify(repasseRepository).save(captor.capture());
        assertEquals(10L, captor.getValue().getAtendimentoId());
        assertEquals(COLABORADOR_ID, captor.getValue().getColaboradorId());
        assertEquals(new BigDecimal("80.00"), captor.getValue().getValor());
        assertEquals(StatusRepasseIndividual.PENDENTE, captor.getValue().getStatus());
    }

    @Test
    void naoDeveCriarRepasseDuplicadoParaAtendimento() {
        when(repasseRepository.existsByAtendimentoId(10L)).thenReturn(true);

        assertThrows(
            RepasseIndividualDadosInvalidosException.class,
            () -> service.criar(new CriarRepasseCommand(10L, COLABORADOR_ID, new BigDecimal("80.00")))
        );

        verify(repasseRepository, never()).save(any(RepasseIndividualEntity.class));
    }

    @Test
    void deveAtualizarRepassePendente() {
        RepasseIndividualEntity repasse = repasse(10L);
        when(repasseRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(Optional.of(repasse));

        UUID novoColaboradorId = UUID.fromString("44444444-4444-4444-4444-444444444444");
        service.atualizar(new AtualizarRepasseCommand(10L, novoColaboradorId, new BigDecimal("90.00")));

        assertEquals(novoColaboradorId, repasse.getColaboradorId());
        assertEquals(new BigDecimal("90.00"), repasse.getValor());
    }

    @Test
    void naoDeveAtualizarRepassePago() {
        RepasseIndividualEntity repasse = repasse(10L);
        repasse.registrarRepasse(UUID.randomUUID(), FormaPagamentoEnum.PIX, null);
        when(repasseRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(Optional.of(repasse));

        assertThrows(
            RepasseIndividualDadosInvalidosException.class,
            () -> service.atualizar(new AtualizarRepasseCommand(10L, COLABORADOR_ID, new BigDecimal("90.00")))
        );
    }

    @Test
    void deveCancelarRepassePendente() {
        RepasseIndividualEntity repasse = repasse(10L);
        when(repasseRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(Optional.of(repasse));

        service.cancelarPorAtendimento(10L);

        assertEquals(StatusRepasseIndividual.CANCELADO, repasse.getStatus());
    }

    @Test
    void naoDeveCancelarRepassePago() {
        RepasseIndividualEntity repasse = repasse(10L);
        repasse.registrarRepasse(UUID.randomUUID(), FormaPagamentoEnum.PIX, null);
        when(repasseRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(Optional.of(repasse));

        assertThrows(RepasseIndividualDadosInvalidosException.class, () -> service.cancelarPorAtendimento(10L));
    }

    @Test
    void deveConsultarPendenciaPorColaborador() {
        when(repasseRepository.existsByColaboradorIdAndStatus(COLABORADOR_ID, StatusRepasseIndividual.PENDENTE))
            .thenReturn(true);

        boolean possuiPendencia = service.possuiPendenciaPorColaboradorId(COLABORADOR_ID);

        assertEquals(true, possuiPendencia);
    }

    @Test
    void deveBuscarResumosEmLote() {
        RepasseIndividualEntity primeiro = repasse(10L);
        RepasseIndividualEntity segundo = repasse(20L);
        when(repasseRepository.findAllByAtendimentoIdIn(Set.of(10L, 20L)))
            .thenReturn(List.of(primeiro, segundo));

        Map<Long, RepasseResumo> resumos = service.buscarResumosPorAtendimentoIds(Set.of(10L, 20L));

        assertEquals(2, resumos.size());
        assertEquals(new BigDecimal("80.00"), resumos.get(10L).valor());
        assertEquals(StatusRepasseIndividual.PENDENTE.name(), resumos.get(20L).status());
    }

    @Test
    void naoDeveConsultarRepositoryParaLoteVazio() {
        Map<Long, RepasseResumo> resumos = service.buscarResumosPorAtendimentoIds(Set.of());

        assertEquals(Map.of(), resumos);
        verify(repasseRepository, never()).findAllByAtendimentoIdIn(any());
    }

    private static RepasseIndividualEntity repasse(Long atendimentoId) {
        return new RepasseIndividualEntity(atendimentoId, COLABORADOR_ID, new BigDecimal("80.00"));
    }
}
