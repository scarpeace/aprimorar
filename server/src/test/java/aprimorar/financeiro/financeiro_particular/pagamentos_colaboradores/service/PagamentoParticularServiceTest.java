package aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.domain.PagamentoParticular;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.domain.exception.PagamentoParticularDadosInvalidosException;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.repository.PagamentoParticularRepository;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.domain.RepasseParticular;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.domain.enums.StatusRepasseParticular;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.api.RepasseParticularDadosInvalidosException;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.repository.RepasseParticularRepository;
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
class PagamentoParticularServiceTest {

    private static final UUID COLABORADOR_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID OUTRO_COLABORADOR_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");

    @Mock
    private PagamentoParticularRepository pagamentoRepository;

    @Mock
    private RepasseParticularRepository repasseRepository;

    private PagamentoParticularService service;

    @BeforeEach
    void setUp() {
        service = new PagamentoParticularService(pagamentoRepository, repasseRepository);
    }

    @Test
    void deveRegistrarPagamentoParaRepassesDoMesmoColaborador() {
        RepasseParticular primeiro = repasse(10L, COLABORADOR_ID, "80.00");
        RepasseParticular segundo = repasse(20L, COLABORADOR_ID, "120.00");
        when(repasseRepository.findAllByIdInForUpdate(List.of(1L, 2L)))
            .thenReturn(List.of(primeiro, segundo));
        when(pagamentoRepository.save(any(PagamentoParticular.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        UUID pagamentoId = service.registrarPagamento(
            List.of(1L, 2L),
            LocalDate.now(),
            FormaPagamentoEnum.PIX,
            null
        );

        ArgumentCaptor<PagamentoParticular> captor = ArgumentCaptor.forClass(PagamentoParticular.class);
        verify(pagamentoRepository).save(captor.capture());
        assertEquals(pagamentoId, captor.getValue().getId());
        assertEquals(new BigDecimal("200.00"), captor.getValue().getValorTotal());
        assertEquals(StatusRepasseParticular.PAGO, primeiro.getStatus());
        assertEquals(StatusRepasseParticular.PAGO, segundo.getStatus());
    }

    @Test
    void naoDeveRegistrarPagamentoComRepassesDeColaboradoresDiferentes() {
        RepasseParticular primeiro = repasse(10L, COLABORADOR_ID, "80.00");
        RepasseParticular segundo = repasse(20L, OUTRO_COLABORADOR_ID, "120.00");
        when(repasseRepository.findAllByIdInForUpdate(List.of(1L, 2L)))
            .thenReturn(List.of(primeiro, segundo));

        assertThrows(
            PagamentoParticularDadosInvalidosException.class,
            () -> service.registrarPagamento(
                List.of(1L, 2L),
                LocalDate.now(),
                FormaPagamentoEnum.PIX,
                null
            )
        );
    }

    @Test
    void naoDeveRegistrarPagamentoComRepasseJaPago() {
        RepasseParticular repasse = repasse(10L, COLABORADOR_ID, "80.00");
        repasse.vincularPagamento(new PagamentoParticular(
            LocalDate.now(),
            new BigDecimal("80.00"),
            FormaPagamentoEnum.PIX,
            null
        ));
        when(repasseRepository.findAllByIdInForUpdate(List.of(1L)))
            .thenReturn(List.of(repasse));

        assertThrows(
            RepasseParticularDadosInvalidosException.class,
            () -> service.registrarPagamento(
                List.of(1L),
                LocalDate.now(),
                FormaPagamentoEnum.PIX,
                null
            )
        );
    }

    @Test
    void deveCancelarPagamentoEDeixarRepassesPendentes() {
        RepasseParticular repasse = repasse(10L, COLABORADOR_ID, "80.00");
        PagamentoParticular pagamento = new PagamentoParticular(
            LocalDate.now(),
            new BigDecimal("80.00"),
            FormaPagamentoEnum.PIX,
            null
        );
        repasse.vincularPagamento(pagamento);
        when(pagamentoRepository.findByIdForUpdate(pagamento.getId()))
            .thenReturn(Optional.of(pagamento));
        when(repasseRepository.findAllByPagamentoIdForUpdate(pagamento.getId()))
            .thenReturn(List.of(repasse));

        service.cancelarPagamento(pagamento.getId());

        assertEquals(StatusRepasseParticular.PENDENTE, repasse.getStatus());
        verify(repasseRepository).flush();
        verify(pagamentoRepository).delete(pagamento);
    }

    private static RepasseParticular repasse(
        Long atendimentoId,
        UUID colaboradorId,
        String valor
    ) {
        return new RepasseParticular(atendimentoId, colaboradorId, new BigDecimal(valor));
    }
}
