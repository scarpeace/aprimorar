package aprimorar.atendimentos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.dto.AtendimentoFiltroRequest;
import aprimorar.atendimentos.dto.CalendarioAtendimentosResponse;
import aprimorar.atendimentos.dto.ColaboradorResumoFinanceiroResponse;
import aprimorar.atendimentos.enums.TipoAtendimento;
import aprimorar.atendimentos.repository.AtendimentoRepository;
import aprimorar.atendimentos.repository.projections.AtendimentosReportProjection;
import aprimorar.exception.BusinessException;
import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.domain.Colaborador;
import aprimorar.pessoas.domain.Endereco;
import aprimorar.pessoas.domain.Responsavel;
import aprimorar.pessoas.repository.AlunoRepository;
import aprimorar.pessoas.repository.ColaboradorRepository;
import aprimorar.pessoas.shared.FuncoesColaborador;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AtendimentoQueryServiceTest {

    private static final UUID ALUNO_ID = UUID.fromString("11111111-1111-4111-8111-111111111111");
    private static final UUID COLABORADOR_ID = UUID.fromString("22222222-2222-4222-8222-222222222222");
    private static final UUID RESPONSAVEL_ID = UUID.fromString("33333333-3333-4333-8333-333333333333");

    @Mock
    private AtendimentoRepository atendimentoRepo;

    @Mock
    private AlunoRepository alunoRepo;

    @Mock
    private ColaboradorRepository colaboradorRepo;

    private AtendimentoQueryService service;

    @BeforeEach
    void setUp() {
        service = new AtendimentoQueryService(atendimentoRepo, alunoRepo, colaboradorRepo);
    }

    @Test
    void shouldGetAtendimentos() {
        var atendimento = atendimento();
        var pageable = PageRequest.of(0, 10);

        when(atendimentoRepo.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(List.of(atendimento), pageable, 1));

        var response = service.getAtendimentos(pageable, new AtendimentoFiltroRequest(null, null, null, null, null, null, null, null, null, null, null, null));

        assertEquals(1, response.getTotalElements());
        assertEquals(atendimento.getId(), response.getContent().getFirst().id());
        assertEquals("Ana Silva", response.getContent().getFirst().nomeAluno());
    }

    @Test
    void shouldFindAtendimentoById() {
        var atendimento = atendimento();

        when(atendimentoRepo.findById(1L)).thenReturn(Optional.of(atendimento));

        var response = service.findAtendimentoById(1L);

        assertEquals(1L, response.id());
        assertEquals(ALUNO_ID, response.alunoId());
    }

    @Test
    void shouldThrowWhenFindAtendimentoByIdDoesNotExist() {
        when(atendimentoRepo.findById(1L)).thenReturn(Optional.empty());

        var ex = assertThrows(BusinessException.class, () -> service.findAtendimentoById(1L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Atendimento não encontrado", ex.getMessage());
    }

    @Test
    void shouldGetRelatorioAtendimentos() {
        when(atendimentoRepo.getRelatorioMensal(any(), any())).thenReturn(reportProjection());

        var response = service.getRelatorioAtendimentos(YearMonth.of(2026, 7));

        assertEquals(28, response.totalAtendimentos());
        assertEquals(10, response.totalAulas());
        assertEquals(2, response.totalOutros());
    }

    @Test
    void shouldGetCalendarioAtendimentos() {
        var inicio = LocalDateTime.of(2026, 7, 10, 14, 0);
        var evento = new CalendarioAtendimentosResponse(
            1L,
            ALUNO_ID,
            "Ana Silva",
            COLABORADOR_ID,
            "Lucas Almeida",
            inicio,
            inicio.plusHours(1),
            TipoAtendimento.AULA
        );

        when(atendimentoRepo.getRelatorioMensal(any(), any())).thenReturn(reportProjection());
        when(atendimentoRepo.getCalendarioMensal(any(), any())).thenReturn(List.of(evento));

        var response = service.getCalendarioAtendimentos(YearMonth.of(2026, 7));

        assertEquals(YearMonth.of(2026, 7), response.anoMes());
        assertEquals(28, response.totalAtendimentos());
        assertEquals(1, response.eventos().size());
        assertEquals("Ana Silva", response.eventos().getFirst().nomeAluno());
    }

    @Test
    void shouldGetResumoFinanceiroColaborador() {
        var inicio = LocalDate.of(2026, 7, 1);
        var fim = LocalDate.of(2026, 7, 31);
        var resumo = new ColaboradorResumoFinanceiroResponse(
            2L,
            BigDecimal.valueOf(300),
            BigDecimal.valueOf(100),
            BigDecimal.valueOf(200)
        );

        when(colaboradorRepo.existsById(COLABORADOR_ID)).thenReturn(true);
        when(atendimentoRepo.getResumoFinanceiroColaborador(eq(COLABORADOR_ID), any(), any())).thenReturn(resumo);

        var response = service.getResumoFinanceiroColaborador(COLABORADOR_ID, inicio, fim);

        assertEquals(2, response.totalAtendimentos());
        assertEquals(BigDecimal.valueOf(300), response.valorTotal());
    }

    @Test
    void shouldThrowWhenResumoFinanceiroColaboradorHasInvalidPeriod() {
        var inicio = LocalDate.of(2026, 7, 31);
        var fim = LocalDate.of(2026, 7, 1);

        var ex = assertThrows(BusinessException.class, () -> service.getResumoFinanceiroColaborador(COLABORADOR_ID, inicio, fim));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("A data final não pode ser anterior à data inicial", ex.getMessage());
    }

    @Test
    void shouldThrowWhenResumoFinanceiroColaboradorDoesNotExist() {
        var inicio = LocalDate.of(2026, 7, 1);
        var fim = LocalDate.of(2026, 7, 31);

        when(colaboradorRepo.existsById(COLABORADOR_ID)).thenReturn(false);

        var ex = assertThrows(BusinessException.class, () -> service.getResumoFinanceiroColaborador(COLABORADOR_ID, inicio, fim));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Colaborador não encontrado", ex.getMessage());
    }

    @Test
    void shouldGetResumoFinanceiroAluno() {
        when(atendimentoRepo.getTotalPagamentoPagoAluno(eq(ALUNO_ID), any(), any())).thenReturn(BigDecimal.valueOf(100));
        when(atendimentoRepo.getTotalPagamentoPendenteAluno(eq(ALUNO_ID), any(), any())).thenReturn(BigDecimal.valueOf(200));

        var response = service.getResumoFinanceiroAluno(ALUNO_ID, YearMonth.of(2026, 7));

        assertEquals(BigDecimal.valueOf(100), response.totalPago());
        assertEquals(BigDecimal.valueOf(200), response.totalPendente());
    }

    @Test
    void shouldGetRelatorioAluno() {
        var atendimentoPago = atendimento(1L, BigDecimal.valueOf(100));
        atendimentoPago.togglePagamentoAluno();
        var atendimentoPendente = atendimento(2L, BigDecimal.valueOf(200));

        when(alunoRepo.findById(ALUNO_ID)).thenReturn(Optional.of(aluno()));
        when(atendimentoRepo.findRelatorioAluno(eq(ALUNO_ID), any(), any())).thenReturn(List.of(atendimentoPago, atendimentoPendente));

        var response = service.getRelatorioAluno(ALUNO_ID, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 31));

        assertEquals("Ana Silva", response.aluno().nome());
        assertEquals("João Pereira", response.responsavel().nome());
        assertEquals(2, response.resumo().totalAtendimentos());
        assertEquals(BigDecimal.valueOf(300), response.resumo().valorTotal());
        assertEquals(BigDecimal.valueOf(100), response.resumo().valorPago());
        assertEquals(BigDecimal.valueOf(200), response.resumo().valorPendente());
        assertEquals("Lucas Almeida", response.atendimentos().getFirst().nomeColaborador());
    }

    @Test
    void shouldThrowWhenRelatorioAlunoHasInvalidPeriod() {
        var inicio = LocalDate.of(2026, 7, 31);
        var fim = LocalDate.of(2026, 7, 1);

        var ex = assertThrows(BusinessException.class, () -> service.getRelatorioAluno(ALUNO_ID, inicio, fim));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("A data final não pode ser anterior à data inicial", ex.getMessage());
    }

    @Test
    void shouldThrowWhenRelatorioAlunoDoesNotExist() {
        var inicio = LocalDate.of(2026, 7, 1);
        var fim = LocalDate.of(2026, 7, 31);

        when(alunoRepo.findById(ALUNO_ID)).thenReturn(Optional.empty());

        var ex = assertThrows(BusinessException.class, () -> service.getRelatorioAluno(ALUNO_ID, inicio, fim));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Aluno não encontrado", ex.getMessage());
    }

    private static AtendimentosReportProjection reportProjection() {
        return new AtendimentosReportProjection() {
            @Override
            public Long getTotalAulas() {
                return 10L;
            }

            @Override
            public Long getTotalMentoria() {
                return 5L;
            }

            @Override
            public Long getTotalTerapia() {
                return 4L;
            }

            @Override
            public Long getTotalOV() {
                return 3L;
            }

            @Override
            public Long getTotalENEM() {
                return 2L;
            }

            @Override
            public Long getTotalPAS() {
                return 2L;
            }

            @Override
            public Long getTotalOutros() {
                return 2L;
            }
        };
    }

    private static Atendimento atendimento() {
        return atendimento(1L, BigDecimal.valueOf(150));
    }

    private static Atendimento atendimento(Long id, BigDecimal pagamentoAluno) {
        var inicio = LocalDateTime.of(2026, 7, 10, 14, 0);
        var atendimento = new Atendimento(
            inicio,
            inicio.plusHours(1),
            TipoAtendimento.AULA,
            aluno(),
            colaborador(),
            pagamentoAluno,
            BigDecimal.valueOf(100)
        );
        setId(atendimento, id);
        return atendimento;
    }

    private static Aluno aluno() {
        var aluno = new Aluno(
            "Ana Silva",
            LocalDate.of(2010, 1, 1),
            "61999999999",
            "12345678900",
            "ana@example.com",
            "Colégio Aprimorar",
            responsavel(),
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000000", "Apto 1")
        );
        setId(aluno, ALUNO_ID);
        return aluno;
    }

    private static Colaborador colaborador() {
        var colaborador = new Colaborador(
            "Lucas Almeida",
            LocalDate.of(1990, 5, 21),
            "lucas.pix@example.com",
            "61999998888",
            "98765432100",
            "lucas@example.com",
            FuncoesColaborador.PROFESSOR,
            new Endereco("Rua B", "20", "Centro", "Brasilia", "DF", "71000000", null)
        );
        setId(colaborador, COLABORADOR_ID);
        return colaborador;
    }

    private static Responsavel responsavel() {
        var responsavel = new Responsavel(
            "João Pereira",
            LocalDate.of(1980, 5, 21),
            "61988887777",
            "45678912300",
            "joao@example.com"
        );
        setId(responsavel, RESPONSAVEL_ID);
        return responsavel;
    }

    private static void setId(Atendimento atendimento, Long id) {
        ReflectionTestUtils.setField(atendimento, "id", id);
    }

    private static void setId(Aluno aluno, UUID id) {
        ReflectionTestUtils.setField(aluno, "id", id);
    }

    private static void setId(Colaborador colaborador, UUID id) {
        ReflectionTestUtils.setField(colaborador, "id", id);
    }

    private static void setId(Responsavel responsavel, UUID id) {
        ReflectionTestUtils.setField(responsavel, "id", id);
    }
}
