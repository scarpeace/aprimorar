package aprimorar.financeiro.repasses_colaboradores.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.repasses_colaboradores.domain.RepassePagamento;
import aprimorar.financeiro.repasses_colaboradores.domain.Repasse;
import aprimorar.financeiro.repasses_colaboradores.domain.enums.StatusRepasse;
import aprimorar.financeiro.repasses_colaboradores.domain.exception.PagamentoDadosInvalidosException;
import aprimorar.financeiro.repasses_colaboradores.domain.exception.RepasseDadosInvalidosException;
import aprimorar.financeiro.repasses_colaboradores.infrastructure.PagamentoRepository;
import aprimorar.financeiro.repasses_colaboradores.infrastructure.RepasseRepository;
import aprimorar.financeiro.repasses_colaboradores.web.dto.RegistrarPagamentoRequest;

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
class PagamentoServiceTest {

    private static final UUID COLABORADOR_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID OUTRO_COLABORADOR_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private RepasseRepository repasseRepository;

    private PagamentoService service;

    @BeforeEach
    void setUp() {
        service = new PagamentoService(pagamentoRepository, repasseRepository);
    }

    @Test
    void deveRegistrarPagamentoParaRepassesDoMesmoColaborador() {
        Repasse primeiro = repasse(10L, COLABORADOR_ID, "80.00");
        Repasse segundo = repasse(20L, COLABORADOR_ID, "120.00");
        when(repasseRepository.findAllByIdInForUpdate(List.of(1L, 2L)))
            .thenReturn(List.of(primeiro, segundo));
        when(pagamentoRepository.save(any(RepassePagamento.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        UUID pagamentoId = service.registrarPagamento(
            new RegistrarPagamentoRequest(
                List.of(1L, 2L),
                LocalDate.now(),
                FormaPagamentoEnum.PIX,
                null
            )
        );

        ArgumentCaptor<RepassePagamento> captor = ArgumentCaptor.forClass(RepassePagamento.class);
        verify(pagamentoRepository).save(captor.capture());
        assertEquals(pagamentoId, captor.getValue().getId());
        assertEquals(new BigDecimal("200.00"), captor.getValue().getValorTotal());
        assertEquals(StatusRepasse.PAGO, primeiro.getStatus());
        assertEquals(StatusRepasse.PAGO, segundo.getStatus());
    }

    @Test
    void naoDeveRegistrarPagamentoComRepassesDeColaboradoresDiferentes() {
        Repasse primeiro = repasse(10L, COLABORADOR_ID, "80.00");
        Repasse segundo = repasse(20L, OUTRO_COLABORADOR_ID, "120.00");
        when(repasseRepository.findAllByIdInForUpdate(List.of(1L, 2L)))
            .thenReturn(List.of(primeiro, segundo));

        assertThrows(
            PagamentoDadosInvalidosException.class,
            () -> service.registrarPagamento(
                new RegistrarPagamentoRequest(
                    List.of(1L, 2L),
                    LocalDate.now(),
                    FormaPagamentoEnum.PIX,
                    null
                )
            )
        );
    }

    @Test
    void naoDeveRegistrarPagamentoComRepasseJaPago() {
        Repasse repasse = repasse(10L, COLABORADOR_ID, "80.00");
        repasse.vincularPagamento(new RepassePagamento(
            LocalDate.now(),
            new BigDecimal("80.00"),
            FormaPagamentoEnum.PIX,
            null
        ));
        when(repasseRepository.findAllByIdInForUpdate(List.of(1L)))
            .thenReturn(List.of(repasse));

        assertThrows(
            RepasseDadosInvalidosException.class,
            () -> service.registrarPagamento(
                new RegistrarPagamentoRequest(
                    List.of(1L),
                    LocalDate.now(),
                    FormaPagamentoEnum.PIX,
                    null
                )
            )
        );
    }

    @Test
    void deveCancelarPagamentoEDeixarRepassesPendentes() {
        Repasse repasse = repasse(10L, COLABORADOR_ID, "80.00");
        RepassePagamento pagamento = new RepassePagamento(
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

        assertEquals(StatusRepasse.PENDENTE, repasse.getStatus());
        verify(repasseRepository).flush();
        verify(pagamentoRepository).delete(pagamento);
    }

    private static Repasse repasse(
        Long atendimentoId,
        UUID colaboradorId,
        String valor
    ) {
        return new Repasse(atendimentoId, colaboradorId, new BigDecimal(valor));
    }
}
