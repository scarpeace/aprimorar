package aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.pagamentos_colaboradores.api.commands.AtualizarRepasseCommandApi;
import aprimorar.financeiro.pagamentos_colaboradores.api.commands.CriarRepasseCommandApi;
import aprimorar.financeiro.pagamentos_colaboradores.application.RepasseParticularService;
import aprimorar.financeiro.pagamentos_colaboradores.domain.Pagamento;
import aprimorar.financeiro.pagamentos_colaboradores.domain.Repasse;
import aprimorar.financeiro.pagamentos_colaboradores.domain.enums.StatusRepasse;
import aprimorar.financeiro.pagamentos_colaboradores.domain.exception.RepasseDadosInvalidosException;
import aprimorar.financeiro.pagamentos_colaboradores.infrastructure.RepasseRepository;

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
    private RepasseRepository repasseRepository;

    private RepasseParticularService service;

    @BeforeEach
    void setUp() {
        service = new RepasseParticularService(repasseRepository);
    }

    @Test
    void deveCriarRepassePendente() {
        service.criar(new CriarRepasseCommandApi(10L, COLABORADOR_ID, new BigDecimal("80.00")));

        ArgumentCaptor<Repasse> captor = ArgumentCaptor.forClass(Repasse.class);
        verify(repasseRepository).save(captor.capture());
        assertEquals(10L, captor.getValue().getAtendimentoId());
        assertEquals(COLABORADOR_ID, captor.getValue().getColaboradorId());
        assertEquals(new BigDecimal("80.00"), captor.getValue().getValor());
        assertEquals(StatusRepasse.PENDENTE, captor.getValue().getStatus());
    }

    @Test
    void deveAtualizarRepassePendente() {
        Repasse repasse = repasse(10L);
        when(repasseRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(java.util.Optional.of(repasse));

        UUID novoColaboradorId = UUID.fromString("44444444-4444-4444-4444-444444444444");
        service.atualizar(new AtualizarRepasseCommandApi(10L, novoColaboradorId, new BigDecimal("90.00")));

        assertEquals(novoColaboradorId, repasse.getColaboradorId());
        assertEquals(new BigDecimal("90.00"), repasse.getValor());
    }

    @Test
    void naoDeveAtualizarRepassePago() {
        Repasse repasse = repasse(10L);
        repasse.vincularPagamento(new Pagamento(
            LocalDate.now(),
            new BigDecimal("80.00"),
            FormaPagamentoEnum.PIX,
            null
        ));
        when(repasseRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(java.util.Optional.of(repasse));

        assertThrows(
            RepasseDadosInvalidosException.class,
            () -> service.atualizar(new AtualizarRepasseCommandApi(10L, COLABORADOR_ID, new BigDecimal("90.00")))
        );
    }

    @Test
    void deveCancelarRepassePendente() {
        Repasse repasse = repasse(10L);
        when(repasseRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(java.util.Optional.of(repasse));

        service.cancelarPorAtendimento(10L);

        assertEquals(StatusRepasse.CANCELADO, repasse.getStatus());
    }

    @Test
    void naoDeveCancelarRepassePago() {
        Repasse repasse = repasse(10L);
        repasse.vincularPagamento(new Pagamento(
            LocalDate.now(),
            new BigDecimal("80.00"),
            FormaPagamentoEnum.PIX,
            null
        ));
        when(repasseRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(java.util.Optional.of(repasse));

        assertThrows(RepasseDadosInvalidosException.class, () -> service.cancelarPorAtendimento(10L));
    }

    @Test
    void deveConsultarPendenciaPorColaborador() {
        when(repasseRepository.existsByColaboradorIdAndStatus(COLABORADOR_ID, StatusRepasse.PENDENTE))
            .thenReturn(true);

        boolean possuiPendencia = service.possuiPendenciaPorColaboradorId(COLABORADOR_ID);

        assertEquals(true, possuiPendencia);
    }

    private static Repasse repasse(Long atendimentoId) {
        return new Repasse(atendimentoId, COLABORADOR_ID, new BigDecimal("80.00"));
    }
}
