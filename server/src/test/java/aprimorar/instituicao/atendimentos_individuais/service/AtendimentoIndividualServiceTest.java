package aprimorar.instituicao.atendimentos_individuais.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import aprimorar.financeiro.api.cobrancas.CobrancaApi;
import aprimorar.financeiro.api.cobrancas.CriarCobrancaCommand;
import aprimorar.financeiro.api.cobrancas.AtualizarCobrancaCommand;
import aprimorar.financeiro.api.repasses.AtualizarRepasseCommand;
import aprimorar.financeiro.api.repasses.CriarRepasseCommand;
import aprimorar.financeiro.api.repasses.RepasseApi;
import aprimorar.instituicao.alunos.domain.Aluno;
import aprimorar.instituicao.alunos.domain.Responsavel;
import aprimorar.instituicao.atendimentos_individuais.domain.AtendimentoIndividual;
import aprimorar.instituicao.atendimentos_individuais.domain.enums.StatusAtendimentoIndividual;
import aprimorar.instituicao.atendimentos_individuais.domain.enums.TipoAtendimento;
import aprimorar.instituicao.atendimentos_individuais.domain.exception.AtendimentoIndividualDadosInvalidosException;
import aprimorar.instituicao.atendimentos_individuais.repository.AtendimentoIndividualRepository;
import aprimorar.instituicao.atendimentos_individuais.web.dto.atendimento.AtendimentoIndividualRequest;
import aprimorar.instituicao.colaboradores.domain.Colaborador;
import aprimorar.instituicao.colaboradores.domain.enums.FuncoesColaborador;
import aprimorar.instituicao.common.domain.Endereco;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AtendimentoIndividualServiceTest {

    @Mock
    private AtendimentoIndividualRepository atendimentoRepository;

    @Mock
    private CobrancaApi cobrancaApi;

    @Mock
    private RepasseApi repasseApi;

    @Mock
    private aprimorar.instituicao.alunos.service.AlunoServiceImpl alunoService;

    @Mock
    private aprimorar.instituicao.colaboradores.service.ColaboradorServiceImpl colaboradorService;

    private AtendimentoIndividualService service;

    @BeforeEach
    void setUp() {
        service = new AtendimentoIndividualService(
            atendimentoRepository,
            cobrancaApi,
            repasseApi,
            alunoService,
            colaboradorService
        );
    }

    @Test
    void shouldRejectSchedulingWithInactiveAluno() {
        UUID alunoId = UUID.randomUUID();
        UUID colaboradorId = UUID.randomUUID();
        Aluno aluno = aluno();
        aluno.deactivate();

        when(alunoService.findEntityById(alunoId)).thenReturn(Optional.of(aluno));

        AtendimentoIndividualDadosInvalidosException exception = assertThrows(
            AtendimentoIndividualDadosInvalidosException.class,
            () -> service.agendar(request(alunoId, colaboradorId))
        );

        assertEquals("Aluno informado está inativo", exception.getMessage());
        verify(colaboradorService, never()).findEntityById(any());
        verifyNoInteractions(atendimentoRepository, cobrancaApi, repasseApi);
    }

    @Test
    void shouldRejectSchedulingWithInactiveColaborador() {
        UUID alunoId = UUID.randomUUID();
        UUID colaboradorId = UUID.randomUUID();
        Aluno aluno = aluno();
        Colaborador colaborador = colaborador();
        colaborador.deactivate();

        when(alunoService.findEntityById(alunoId)).thenReturn(Optional.of(aluno));
        when(colaboradorService.findEntityById(colaboradorId)).thenReturn(Optional.of(colaborador));

        AtendimentoIndividualDadosInvalidosException exception = assertThrows(
            AtendimentoIndividualDadosInvalidosException.class,
            () -> service.agendar(request(alunoId, colaboradorId))
        );

        assertEquals("Colaborador informado está inativo", exception.getMessage());
        verify(atendimentoRepository, never()).save(any());
        verifyNoInteractions(cobrancaApi, repasseApi);
    }

    @Test
    void shouldCreateAttendanceAndFinancialEntries() {
        UUID alunoId = UUID.randomUUID();
        UUID colaboradorId = UUID.randomUUID();
        Long atendimentoId = 10L;
        Aluno aluno = aluno();
        Colaborador colaborador = colaborador();
        ReflectionTestUtils.setField(aluno, "id", alunoId);
        ReflectionTestUtils.setField(colaborador, "id", colaboradorId);
        AtendimentoIndividual atendimento = atendimento(aluno, colaborador);
        ReflectionTestUtils.setField(atendimento, "id", atendimentoId);

        when(alunoService.findEntityById(alunoId)).thenReturn(Optional.of(aluno));
        when(colaboradorService.findEntityById(colaboradorId)).thenReturn(Optional.of(colaborador));
        when(atendimentoRepository.save(any())).thenReturn(atendimento);

        Long result = service.agendar(request(alunoId, colaboradorId));

        assertEquals(atendimentoId, result);

        ArgumentCaptor<CriarCobrancaCommand> cobrancaCaptor = ArgumentCaptor.forClass(CriarCobrancaCommand.class);
        verify(cobrancaApi).criar(cobrancaCaptor.capture());
        assertEquals(atendimentoId, cobrancaCaptor.getValue().atendimentoId());
        assertEquals(aluno.getId(), cobrancaCaptor.getValue().alunoId());
        assertEquals(BigDecimal.valueOf(150), cobrancaCaptor.getValue().valor());

        ArgumentCaptor<CriarRepasseCommand> repasseCaptor = ArgumentCaptor.forClass(CriarRepasseCommand.class);
        verify(repasseApi).criar(repasseCaptor.capture());
        assertEquals(atendimentoId, repasseCaptor.getValue().atendimentoId());
        assertEquals(colaborador.getId(), repasseCaptor.getValue().colaboradorId());
        assertEquals(BigDecimal.valueOf(100), repasseCaptor.getValue().valor());
    }

    @Test
    void shouldUpdateAttendanceAndFinancialEntries() {
        UUID alunoId = UUID.randomUUID();
        UUID colaboradorId = UUID.randomUUID();
        Long atendimentoId = 10L;
        Aluno aluno = aluno();
        Colaborador colaborador = colaborador();
        ReflectionTestUtils.setField(aluno, "id", alunoId);
        ReflectionTestUtils.setField(colaborador, "id", colaboradorId);
        AtendimentoIndividual atendimento = atendimento(aluno, colaborador);
        ReflectionTestUtils.setField(atendimento, "id", atendimentoId);

        when(atendimentoRepository.findById(atendimentoId)).thenReturn(Optional.of(atendimento));
        when(alunoService.findEntityById(alunoId)).thenReturn(Optional.of(aluno));
        when(colaboradorService.findEntityById(colaboradorId)).thenReturn(Optional.of(colaborador));

        service.update(atendimentoId, request(alunoId, colaboradorId));

        ArgumentCaptor<AtualizarCobrancaCommand> cobrancaCaptor =
            ArgumentCaptor.forClass(AtualizarCobrancaCommand.class);
        verify(cobrancaApi).atualizar(cobrancaCaptor.capture());
        assertEquals(atendimentoId, cobrancaCaptor.getValue().atendimentoId());
        assertEquals(aluno.getId(), cobrancaCaptor.getValue().alunoId());
        assertEquals(BigDecimal.valueOf(150), cobrancaCaptor.getValue().valor());

        ArgumentCaptor<AtualizarRepasseCommand> repasseCaptor =
            ArgumentCaptor.forClass(AtualizarRepasseCommand.class);
        verify(repasseApi).atualizar(repasseCaptor.capture());
        assertEquals(atendimentoId, repasseCaptor.getValue().atendimentoId());
        assertEquals(colaborador.getId(), repasseCaptor.getValue().colaboradorId());
        assertEquals(BigDecimal.valueOf(100), repasseCaptor.getValue().valor());
        assertEquals(TipoAtendimento.AULA, atendimento.getTipo());
    }

    @Test
    void shouldMarkAttendanceAsCompleted() {
        Long atendimentoId = 10L;
        AtendimentoIndividual atendimento = atendimento(aluno(), colaborador());
        ReflectionTestUtils.setField(atendimento, "id", atendimentoId);
        when(atendimentoRepository.findById(atendimentoId)).thenReturn(Optional.of(atendimento));

        service.realizar(atendimentoId);

        assertEquals(StatusAtendimentoIndividual.REALIZADO, atendimento.getStatus());
        verifyNoInteractions(cobrancaApi, repasseApi);
    }

    @Test
    void shouldCancelAttendanceAndFinancialEntries() {
        Long atendimentoId = 10L;
        AtendimentoIndividual atendimento = atendimento(aluno(), colaborador());
        ReflectionTestUtils.setField(atendimento, "id", atendimentoId);
        when(atendimentoRepository.findById(atendimentoId)).thenReturn(Optional.of(atendimento));

        service.cancelar(atendimentoId);

        assertEquals(StatusAtendimentoIndividual.CANCELADO, atendimento.getStatus());
        verify(cobrancaApi).cancelarPorAtendimento(atendimentoId);
        verify(repasseApi).cancelarPorAtendimento(atendimentoId);
    }

    private static AtendimentoIndividualRequest request(UUID alunoId, UUID colaboradorId) {
        return new AtendimentoIndividualRequest(
            TipoAtendimento.AULA,
            LocalDateTime.of(2026, 9, 18, 10, 0),
            LocalDateTime.of(2026, 9, 18, 11, 0),
            BigDecimal.valueOf(150),
            BigDecimal.valueOf(100),
            alunoId,
            colaboradorId
        );
    }

    private static AtendimentoIndividual atendimento(Aluno aluno, Colaborador colaborador) {
        return new AtendimentoIndividual(
            LocalDateTime.of(2026, 9, 18, 10, 0),
            LocalDateTime.of(2026, 9, 18, 11, 0),
            TipoAtendimento.AULA,
            aluno,
            colaborador
        );
    }

    private static Aluno aluno() {
        return new Aluno(
            "Ana Silva",
            LocalDate.of(2010, 1, 1),
            "61999999999",
            "12345678900",
            "ana@example.com",
            "Colégio Aprimorar",
            new Responsavel("João Pereira", "61999999999", "98765432100", "joao@example.com"),
            endereco()
        );
    }

    private static Colaborador colaborador() {
        return new Colaborador(
            "João Pereira",
            LocalDate.of(1990, 5, 21),
            "61999999999",
            "61988888888",
            "98765432100",
            "joao@example.com",
            FuncoesColaborador.PROFESSOR,
            endereco()
        );
    }

    private static Endereco endereco() {
        return new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000000", "Apto 1");
    }
}
