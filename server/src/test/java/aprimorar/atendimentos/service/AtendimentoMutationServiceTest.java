package aprimorar.atendimentos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.dto.AtendimentoRequest;
import aprimorar.atendimentos.dto.ReagendarAtendimentoRequest;
import aprimorar.atendimentos.enums.StatusAtendimento;
import aprimorar.atendimentos.enums.TipoAtendimento;
import aprimorar.atendimentos.repository.AtendimentoRepository;
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
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AtendimentoMutationServiceTest {

    private static final UUID ALUNO_ID = UUID.fromString("11111111-1111-4111-8111-111111111111");
    private static final UUID COLABORADOR_ID = UUID.fromString("22222222-2222-4222-8222-222222222222");

    @Mock
    private AtendimentoRepository atendimentoRepo;

    @Mock
    private AlunoRepository alunoRepo;

    @Mock
    private ColaboradorRepository colaboradorRepo;

    private AtendimentoMutationService service;

    @BeforeEach
    void setUp() {
        service = new AtendimentoMutationService(atendimentoRepo, alunoRepo, colaboradorRepo);
    }

    @Test
    void shouldAgendarAtendimento() {
        var request = atendimentoRequest();
        var aluno = aluno();
        var colaborador = colaborador();

        when(alunoRepo.findById(ALUNO_ID)).thenReturn(Optional.of(aluno));
        when(colaboradorRepo.findById(COLABORADOR_ID)).thenReturn(Optional.of(colaborador));
        when(atendimentoRepo.alunoPossuiAtendimentoConflitante(any(), any(), any(), any())).thenReturn(false);
        when(atendimentoRepo.colaboradorPossuiAtendimentoConflitante(any(), any(), any(), any())).thenReturn(false);
        when(atendimentoRepo.save(any(Atendimento.class))).thenAnswer(invocation -> {
            var atendimento = invocation.getArgument(0, Atendimento.class);
            setId(atendimento, 1L);
            return atendimento;
        });

        var response = service.agendar(request);

        assertEquals(1L, response.id());
        assertEquals(TipoAtendimento.AULA, response.tipo());
        assertEquals(ALUNO_ID, response.alunoId());
        assertEquals(COLABORADOR_ID, response.colaboradorId());

        ArgumentCaptor<Atendimento> captor = ArgumentCaptor.forClass(Atendimento.class);
        verify(atendimentoRepo).save(captor.capture());
        assertEquals(BigDecimal.valueOf(150), captor.getValue().getPagamentoAluno());
    }

    @Test
    void shouldThrowWhenAgendarAndAlunoDoesNotExist() {
        var request = atendimentoRequest();

        when(alunoRepo.findById(ALUNO_ID)).thenReturn(Optional.empty());

        var ex = assertThrows(BusinessException.class, () -> service.agendar(request));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Aluno não encontrado", ex.getMessage());
        verify(atendimentoRepo, never()).save(any());
    }

    @Test
    void shouldThrowWhenAgendarAndColaboradorDoesNotExist() {
        var request = atendimentoRequest();

        when(alunoRepo.findById(ALUNO_ID)).thenReturn(Optional.of(aluno()));
        when(colaboradorRepo.findById(COLABORADOR_ID)).thenReturn(Optional.empty());

        var ex = assertThrows(BusinessException.class, () -> service.agendar(request));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Colaborador não encontrado", ex.getMessage());
        verify(atendimentoRepo, never()).save(any());
    }

    @Test
    void shouldThrowWhenAgendarAndAlunoHasConflict() {
        var request = atendimentoRequest();

        when(alunoRepo.findById(ALUNO_ID)).thenReturn(Optional.of(aluno()));
        when(colaboradorRepo.findById(COLABORADOR_ID)).thenReturn(Optional.of(colaborador()));
        when(atendimentoRepo.alunoPossuiAtendimentoConflitante(any(), any(), any(), any())).thenReturn(true);

        var ex = assertThrows(BusinessException.class, () -> service.agendar(request));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("O aluno informado ja possui um atendimento no intervalo", ex.getMessage());
        verify(atendimentoRepo, never()).save(any());
    }

    @Test
    void shouldThrowWhenAgendarAndColaboradorHasConflict() {
        var request = atendimentoRequest();

        when(alunoRepo.findById(ALUNO_ID)).thenReturn(Optional.of(aluno()));
        when(colaboradorRepo.findById(COLABORADOR_ID)).thenReturn(Optional.of(colaborador()));
        when(atendimentoRepo.alunoPossuiAtendimentoConflitante(any(), any(), any(), any())).thenReturn(false);
        when(atendimentoRepo.colaboradorPossuiAtendimentoConflitante(any(), any(), any(), any())).thenReturn(true);

        var ex = assertThrows(BusinessException.class, () -> service.agendar(request));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("O colaborador informado ja possui um atendimento no intervalo", ex.getMessage());
        verify(atendimentoRepo, never()).save(any());
    }

    @Test
    void shouldUpdateAtendimento() {
        var atendimento = atendimento();
        var request = atendimentoRequest(TipoAtendimento.TERAPIA);

        when(atendimentoRepo.findById(1L)).thenReturn(Optional.of(atendimento));
        when(alunoRepo.findById(ALUNO_ID)).thenReturn(Optional.of(aluno()));
        when(colaboradorRepo.findById(COLABORADOR_ID)).thenReturn(Optional.of(colaborador()));
        when(atendimentoRepo.save(atendimento)).thenReturn(atendimento);

        var response = service.update(1L, request);

        assertEquals(TipoAtendimento.TERAPIA, atendimento.getTipo());
        assertEquals(TipoAtendimento.TERAPIA, response.tipo());
        verify(atendimentoRepo).save(atendimento);
    }

    @Test
    void shouldCancelAtendimento() {
        var atendimento = atendimento();

        when(atendimentoRepo.findById(1L)).thenReturn(Optional.of(atendimento));

        service.cancelar(1L);

        assertEquals(StatusAtendimento.CANCELADO, atendimento.getStatus());
    }

    @Test
    void shouldReagendarAtendimento() {
        var atendimento = atendimento();
        var novoInicio = LocalDateTime.now().plusDays(3);
        var novoFim = novoInicio.plusHours(1);

        when(atendimentoRepo.findById(1L)).thenReturn(Optional.of(atendimento));

        service.reagendar(1L, new ReagendarAtendimentoRequest(novoInicio, novoFim));

        assertEquals(novoInicio, atendimento.getDataHoraInicio());
        assertEquals(novoFim, atendimento.getDataHoraFim());
    }

    @Test
    void shouldConcluirAtendimento() {
        var atendimento = atendimento();

        when(atendimentoRepo.findById(1L)).thenReturn(Optional.of(atendimento));

        service.concluir(1L);

        assertEquals(StatusAtendimento.CONCLUIDO, atendimento.getStatus());
    }

    @Test
    void shouldTogglePagamentoAluno() {
        var atendimento = atendimento();

        when(atendimentoRepo.findById(1L)).thenReturn(Optional.of(atendimento));

        var response = service.togglePagamentoAluno(1L);

        assertNotNull(atendimento.getDataPagamentoAluno());
        assertNotNull(response.dataPagamentoAluno());
    }

    @Test
    void shouldToggleRepasseColaborador() {
        var atendimento = atendimento();

        when(atendimentoRepo.findById(1L)).thenReturn(Optional.of(atendimento));

        var response = service.toggleRepasseColaborador(1L);

        assertNotNull(atendimento.getDataRepasseColaborador());
        assertNotNull(response.dataRepasseColaborador());
    }

    @Test
    void shouldAlterarParticipantes() {
        var atendimento = atendimento();
        var novoAluno = aluno("Maria Silva");
        var novoColaborador = colaborador("Pedro Souza");

        when(atendimentoRepo.findById(1L)).thenReturn(Optional.of(atendimento));
        when(alunoRepo.findById(ALUNO_ID)).thenReturn(Optional.of(novoAluno));
        when(colaboradorRepo.findById(COLABORADOR_ID)).thenReturn(Optional.of(novoColaborador));

        service.alterarParticipantes(1L, ALUNO_ID, COLABORADOR_ID);

        assertEquals("Maria Silva", atendimento.getAluno().getNome());
        assertEquals("Pedro Souza", atendimento.getColaborador().getNome());
    }

    @Test
    void shouldDeleteAtendimento() {
        var atendimento = atendimento();

        when(atendimentoRepo.findById(1L)).thenReturn(Optional.of(atendimento));

        service.excluir(1L);

        verify(atendimentoRepo).delete(atendimento);
    }

    @Test
    void shouldThrowWhenAtendimentoDoesNotExist() {
        when(atendimentoRepo.findById(1L)).thenReturn(Optional.empty());

        var ex = assertThrows(BusinessException.class, () -> service.cancelar(1L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Atendimento não encontrado", ex.getMessage());
    }

    private static AtendimentoRequest atendimentoRequest() {
        return atendimentoRequest(TipoAtendimento.AULA);
    }

    private static AtendimentoRequest atendimentoRequest(TipoAtendimento tipo) {
        var inicio = LocalDateTime.now().plusDays(1);
        return new AtendimentoRequest(
            tipo,
            inicio,
            inicio.plusHours(1),
            BigDecimal.valueOf(150),
            BigDecimal.valueOf(100),
            ALUNO_ID,
            COLABORADOR_ID
        );
    }

    private static Atendimento atendimento() {
        var request = atendimentoRequest();
        var atendimento = request.toEntity(aluno(), colaborador());
        setId(atendimento, 1L);
        return atendimento;
    }

    private static Aluno aluno() {
        return aluno("Ana Silva");
    }

    private static Aluno aluno(String nome) {
        var aluno = new Aluno(
            nome,
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
        return colaborador("Lucas Almeida");
    }

    private static Colaborador colaborador(String nome) {
        var colaborador = new Colaborador(
            nome,
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
        return new Responsavel(
            "João Pereira",
            LocalDate.of(1980, 5, 21),
            "61988887777",
            "45678912300",
            "joao@example.com"
        );
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
}
