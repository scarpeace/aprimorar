package aprimorar.financeiro.repasses_colaboradores.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.repasses_colaboradores.api.commands.AtualizarRepasseCommandApi;
import aprimorar.financeiro.repasses_colaboradores.api.commands.CriarRepasseCommandApi;
import aprimorar.financeiro.repasses_colaboradores.domain.RepassePagamento;
import aprimorar.financeiro.repasses_colaboradores.domain.Repasse;
import aprimorar.financeiro.repasses_colaboradores.domain.enums.StatusRepasse;
import aprimorar.financeiro.repasses_colaboradores.domain.exception.RepasseDadosInvalidosException;
import aprimorar.financeiro.repasses_colaboradores.domain.exception.RepasseJaExistenteException;
import aprimorar.financeiro.repasses_colaboradores.infrastructure.RepasseRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
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
class PagamentosApiImplTest {

    private static final UUID COLABORADOR_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    @Mock
    private RepasseRepository repasseRepository;

    private PagamentosApiImpl service;

    @BeforeEach
    void setUp() {
        service = new PagamentosApiImpl(repasseRepository);
    }

    @Test
    void deveCriarRepassePendente() {
        service.criarRepasse(new CriarRepasseCommandApi(10L, COLABORADOR_ID, new BigDecimal("80.00")));

        ArgumentCaptor<Repasse> captor = ArgumentCaptor.forClass(Repasse.class);
        verify(repasseRepository).saveAndFlush(captor.capture());
        assertEquals(10L, captor.getValue().getAtendimentoId());
        assertEquals(COLABORADOR_ID, captor.getValue().getColaboradorId());
        assertEquals(new BigDecimal("80.00"), captor.getValue().getValor());
        assertEquals(StatusRepasse.PENDENTE, captor.getValue().getStatus());
    }

    @Test
    void deveTraduzirRepasseJaExistente() {
        when(repasseRepository.saveAndFlush(any(Repasse.class))).thenThrow(
            violacaoDaConstraint("uk_repasses_atendimento")
        );

        assertThrows(
            RepasseJaExistenteException.class,
            () -> service.criarRepasse(
                new CriarRepasseCommandApi(
                    10L,
                    COLABORADOR_ID,
                    new BigDecimal("80.00")
                )
            )
        );
    }

    @Test
    void deveAtualizarRepassePendente() {
        Repasse repasse = repasse(10L);
        when(repasseRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(java.util.Optional.of(repasse));

        UUID novoColaboradorId = UUID.fromString("44444444-4444-4444-4444-444444444444");
        service.atualizarRepasse(new AtualizarRepasseCommandApi(10L, novoColaboradorId, new BigDecimal("90.00")));

        assertEquals(novoColaboradorId, repasse.getColaboradorId());
        assertEquals(new BigDecimal("90.00"), repasse.getValor());
    }

    @Test
    void naoDeveAtualizarRepassePago() {
        Repasse repasse = repasse(10L);
        repasse.vincularPagamento(new RepassePagamento(
            LocalDate.now(),
            new BigDecimal("80.00"),
            FormaPagamentoEnum.PIX,
            null
        ));
        when(repasseRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(java.util.Optional.of(repasse));

        assertThrows(
            RepasseDadosInvalidosException.class,
            () -> service.atualizarRepasse(new AtualizarRepasseCommandApi(10L, COLABORADOR_ID, new BigDecimal("90.00")))
        );
    }

    @Test
    void deveCancelarRepassePendente() {
        Repasse repasse = repasse(10L);
        when(repasseRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(java.util.Optional.of(repasse));

        service.cancelarRepasse(10L);

        assertEquals(StatusRepasse.CANCELADO, repasse.getStatus());
    }

    @Test
    void naoDeveCancelarRepassePago() {
        Repasse repasse = repasse(10L);
        repasse.vincularPagamento(new RepassePagamento(
            LocalDate.now(),
            new BigDecimal("80.00"),
            FormaPagamentoEnum.PIX,
            null
        ));
        when(repasseRepository.findByAtendimentoIdForUpdate(10L)).thenReturn(java.util.Optional.of(repasse));

        assertThrows(RepasseDadosInvalidosException.class, () -> service.cancelarRepasse(10L));
    }

    @Test
    void deveConsultarPendenciaPorColaborador() {
        when(repasseRepository.existsByColaboradorIdAndStatusIn(
            COLABORADOR_ID,
            List.of(StatusRepasse.PENDENTE, StatusRepasse.ATRASADO)
        ))
            .thenReturn(true);

        boolean possuiPendencia = service.possuiRepassePendente(COLABORADOR_ID);

        assertEquals(true, possuiPendencia);
    }

    private static Repasse repasse(Long atendimentoId) {
        return new Repasse(atendimentoId, COLABORADOR_ID, new BigDecimal("80.00"));
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
