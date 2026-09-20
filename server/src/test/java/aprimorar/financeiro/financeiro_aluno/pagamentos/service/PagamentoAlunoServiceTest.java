package aprimorar.financeiro.financeiro_aluno.pagamentos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.CobrancaAluno;
import aprimorar.financeiro.financeiro_aluno.pagamentos.domain.PagamentoAluno;
import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.enums.StatusCobrancaAluno;
import aprimorar.financeiro.financeiro_aluno.pagamentos.domain.exception.PagamentoAlunoDadosInvalidosException;
import aprimorar.financeiro.financeiro_aluno.cobrancas.repository.CobrancaAlunoRepository;
import aprimorar.financeiro.financeiro_aluno.pagamentos.repository.PagamentoAlunoRepository;
import aprimorar.financeiro.financeiro_aluno.pagamentos.repository.projections.PagamentoAlunoProjection;
import aprimorar.financeiro.financeiro_aluno.pagamentos.web.dto.PagamentoAlunoDetalheResponse;
import aprimorar.financeiro.financeiro_aluno.pagamentos.web.dto.PagamentoAlunoResponse;
import aprimorar.financeiro.financeiro_aluno.pagamentos.web.dto.RegistrarPagamentoAlunoRequest;
import aprimorar.financeiro.common.FormaPagamentoEnum;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PagamentoAlunoServiceTest {

    private static final UUID ALUNO_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Mock
    private CobrancaAlunoRepository cobrancaRepository;

    @Mock
    private PagamentoAlunoRepository pagamentoRepository;

    private PagamentoAlunoService service;

    @BeforeEach
    void setUp() {
        service = new PagamentoAlunoService(cobrancaRepository, pagamentoRepository);
    }

    @Test
    void deveRegistrarUmPagamentoParaVariasCobrancasDoMesmoAluno() {
        CobrancaAluno primeira = cobranca(10L, ALUNO_ID);
        CobrancaAluno segunda = cobranca(20L, ALUNO_ID);
        when(cobrancaRepository.findAllByIdInForUpdate(List.of(1L, 2L)))
            .thenReturn(List.of(primeira, segunda));
        when(pagamentoRepository.save(any(PagamentoAluno.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        service.registrar(new RegistrarPagamentoAlunoRequest(
            List.of(1L, 2L),
            LocalDate.now(),
            FormaPagamentoEnum.PIX,
            null
        ));

        assertEquals(StatusCobrancaAluno.PAGO, primeira.getStatus());
        assertEquals(StatusCobrancaAluno.PAGO, segunda.getStatus());
        assertEquals(primeira.getPagamento(), segunda.getPagamento());
        verify(pagamentoRepository).save(any(PagamentoAluno.class));
    }

    @Test
    void naoDeveRegistrarPagamentoParaAlunosDiferentes() {
        UUID outroAlunoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        when(cobrancaRepository.findAllByIdInForUpdate(List.of(1L, 2L)))
            .thenReturn(List.of(cobranca(10L, ALUNO_ID), cobranca(20L, outroAlunoId)));

        assertThrows(
            PagamentoAlunoDadosInvalidosException.class,
            () -> service.registrar(new RegistrarPagamentoAlunoRequest(
                List.of(1L, 2L),
                LocalDate.now(),
                FormaPagamentoEnum.PIX,
                null
            ))
        );

        verify(pagamentoRepository, never()).save(any(PagamentoAluno.class));
    }

    @Test
    void deveCancelarPagamentoEVoltarCobrancasParaPendente() {
        CobrancaAluno primeira = cobranca(10L, ALUNO_ID);
        CobrancaAluno segunda = cobranca(20L, ALUNO_ID);
        PagamentoAluno pagamento = new PagamentoAluno(
            ALUNO_ID,
            LocalDate.now(),
            FormaPagamentoEnum.PIX,
            null
        );
        primeira.registrarPagamento(pagamento);
        segunda.registrarPagamento(pagamento);

        when(pagamentoRepository.findByIdForUpdate(pagamento.getId())).thenReturn(Optional.of(pagamento));
        when(cobrancaRepository.findAllByPagamentoIdForUpdate(pagamento.getId()))
            .thenReturn(List.of(primeira, segunda));

        service.cancelar(pagamento.getId());

        assertEquals(StatusCobrancaAluno.PENDENTE, primeira.getStatus());
        assertEquals(StatusCobrancaAluno.PENDENTE, segunda.getStatus());
        verify(pagamentoRepository).delete(pagamento);
    }

    @Test
    void deveListarPagamentosDoAluno() {
        PagamentoAlunoProjection projection = mock(PagamentoAlunoProjection.class);
        Pageable pageable = PageRequest.of(0, 10);
        UUID pagamentoId = UUID.randomUUID();
        when(projection.getId()).thenReturn(pagamentoId);
        when(projection.getAlunoId()).thenReturn(ALUNO_ID);
        when(projection.getDataPagamento()).thenReturn(LocalDate.now());
        when(projection.getFormaPagamento()).thenReturn(FormaPagamentoEnum.PIX);
        when(projection.getValorTotal()).thenReturn(new BigDecimal("200.00"));
        when(projection.getQuantidadeCobrancas()).thenReturn(2L);
        when(pagamentoRepository.findPagamentosPorAlunoId(ALUNO_ID, pageable))
            .thenReturn(new PageImpl<>(List.of(projection), pageable, 1));

        Page<PagamentoAlunoResponse> response = service.buscarPagamentos(ALUNO_ID, pageable);

        assertEquals(1, response.getTotalElements());
        assertEquals(pagamentoId, response.getContent().getFirst().id());
        assertEquals(new BigDecimal("200.00"), response.getContent().getFirst().valorTotal());
    }

    @Test
    void deveBuscarDetalheDoPagamentoComCobrancas() {
        PagamentoAluno pagamento = new PagamentoAluno(
            ALUNO_ID,
            LocalDate.now(),
            FormaPagamentoEnum.PIX,
            null
        );
        List<CobrancaAluno> cobrancas = List.of(
            cobranca(10L, ALUNO_ID),
            cobranca(20L, ALUNO_ID)
        );
        when(pagamentoRepository.findById(pagamento.getId())).thenReturn(Optional.of(pagamento));
        when(cobrancaRepository.findAllByPagamentoIdOrderByIdAsc(pagamento.getId()))
            .thenReturn(cobrancas);

        PagamentoAlunoDetalheResponse response = service.buscarPorId(pagamento.getId());

        assertEquals(pagamento.getId(), response.id());
        assertEquals(new BigDecimal("200.00"), response.valorTotal());
        assertEquals(2, response.cobrancas().size());
    }

    private static CobrancaAluno cobranca(Long atendimentoId, UUID alunoId) {
        return new CobrancaAluno(atendimentoId, alunoId, new BigDecimal("100.00"));
    }
}
