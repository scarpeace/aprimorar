package aprimorar.financeiro.repasses_particular.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.financeiro.api.repasses_particular.AtualizarRepasseCommand;
import aprimorar.financeiro.api.repasses_particular.CriarRepasseCommand;
import aprimorar.financeiro.common.FormaPagamentoEnum;
import aprimorar.financeiro.pagamentos_particular.domain.PagamentoParticular;
import aprimorar.financeiro.repasses_particular.domain.RepasseParticular;
import aprimorar.financeiro.repasses_particular.domain.enums.StatusRepasseParticular;
import aprimorar.financeiro.repasses_particular.domain.exception.RepasseParticularDadosInvalidosException;
import aprimorar.financeiro.repasses_particular.repository.RepasseParticularRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RepasseParticularServiceTest {

    private static final UUID COLABORADOR_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    @Mock
    private RepasseParticularRepository repasseRepository;

    private RepasseParticularService service;

    @BeforeEach
    void setUp() {
        service = new RepasseParticularService(repasseRepository);
    }

    @Test
    void deveCriarRepassePendente() {
        when(repasseRepository.existsByAtendimentoId(10L)).thenReturn(false);

        service.criar(new CriarRepasseCommand(10L, COLABORADOR_ID, new BigDecimal("80.00")));

        ArgumentCaptor<RepasseParticular> captor = ArgumentCaptor.forClass(RepasseParticular.class);
        verify(repasseRepository).save(captor.capture());
        assertEquals(10L, captor.getValue().getAtendimentoId());
        assertEquals(COLABORADOR_ID, captor.getValue().getColaboradorId());
        assertEquals(new BigDecimal("80.00"), captor.getValue().getValor());
        assertEquals(StatusRepasseParticular.PENDENTE, captor.getValue().getStatus());
    }

    @Test
    void naoDeveCriarRepasseDuplicadoParaAtendimento() {
        when(repasseRepository.existsByAtendimentoId(10L)).thenReturn(true);

        assertThrows(
            RepasseParticularDadosInvalidosException.class,
            () -> service.criar(new CriarRepasseCommand(10L, COLABORADOR_ID, new BigDecimal("80.00")))
        );

        verify(repasseRepository, never()).save(any(RepasseParticular.class));
    }

    @Test
    void deveAtualizarRepassePendente() {
        RepasseParticular repasse = repasse(10L);
        when(repasseRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(java.util.Optional.of(repasse));

        UUID novoColaboradorId = UUID.fromString("44444444-4444-4444-4444-444444444444");
        service.atualizar(new AtualizarRepasseCommand(10L, novoColaboradorId, new BigDecimal("90.00")));

        assertEquals(novoColaboradorId, repasse.getColaboradorId());
        assertEquals(new BigDecimal("90.00"), repasse.getValor());
    }

    @Test
    void naoDeveAtualizarRepassePago() {
        RepasseParticular repasse = repasse(10L);
        repasse.vincularPagamento(new PagamentoParticular(
            LocalDate.now(),
            new BigDecimal("80.00"),
            FormaPagamentoEnum.PIX,
            null
        ));
        when(repasseRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(java.util.Optional.of(repasse));

        assertThrows(
            RepasseParticularDadosInvalidosException.class,
            () -> service.atualizar(new AtualizarRepasseCommand(10L, COLABORADOR_ID, new BigDecimal("90.00")))
        );
    }

    @Test
    void deveCancelarRepassePendente() {
        RepasseParticular repasse = repasse(10L);
        when(repasseRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(java.util.Optional.of(repasse));

        service.cancelarPorAtendimento(10L);

        assertEquals(StatusRepasseParticular.CANCELADO, repasse.getStatus());
    }

    @Test
    void naoDeveCancelarRepassePago() {
        RepasseParticular repasse = repasse(10L);
        repasse.vincularPagamento(new PagamentoParticular(
            LocalDate.now(),
            new BigDecimal("80.00"),
            FormaPagamentoEnum.PIX,
            null
        ));
        when(repasseRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(java.util.Optional.of(repasse));

        assertThrows(RepasseParticularDadosInvalidosException.class, () -> service.cancelarPorAtendimento(10L));
    }

    @Test
    void deveConsultarPendenciaPorColaborador() {
        when(repasseRepository.existsByColaboradorIdAndStatus(COLABORADOR_ID, StatusRepasseParticular.PENDENTE))
            .thenReturn(true);

        boolean possuiPendencia = service.possuiPendenciaPorColaboradorId(COLABORADOR_ID);

        assertEquals(true, possuiPendencia);
    }

    private static RepasseParticular repasse(Long atendimentoId) {
        return new RepasseParticular(atendimentoId, COLABORADOR_ID, new BigDecimal("80.00"));
    }
}
