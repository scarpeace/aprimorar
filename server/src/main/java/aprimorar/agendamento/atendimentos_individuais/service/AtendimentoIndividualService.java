package aprimorar.agendamento.atendimentos_individuais.service;

import aprimorar.financeiro.api.financeiro_aluno.AtualizarCobrancaAlunoCommand;
import aprimorar.financeiro.api.financeiro_aluno.CobrancaAlunoApi;
import aprimorar.financeiro.api.financeiro_aluno.CriarCobrancaAlunoCommand;
import aprimorar.financeiro.api.financeiro_aluno.TipoOrigemCobrancaAluno;
import aprimorar.financeiro.api.repasses_particular.AtualizarRepasseCommand;
import aprimorar.financeiro.api.repasses_particular.CriarRepasseCommand;
import aprimorar.financeiro.api.repasses_particular.RepasseApi;
import aprimorar.financeiro.api.financeiro_aluno.CobrancaAlunoResumo;
import aprimorar.financeiro.api.repasses_particular.RepasseParticularSummary;
import aprimorar.agendamento.atendimentos_individuais.repository.AtendimentoIndividualRepository;
import aprimorar.agendamento.atendimentos_individuais.repository.AtendimentoIndividualSpecifications;
import aprimorar.agendamento.atendimentos_individuais.web.dto.atendimento.AtendimentoIndividualFiltroRequest;
import aprimorar.agendamento.atendimentos_individuais.web.dto.atendimento.AtendimentoIndividualRequest;
import aprimorar.agendamento.atendimentos_individuais.web.dto.atendimento.AtendimentoIndividualResponse;
import aprimorar.agendamento.atendimentos_individuais.web.dto.atendimento.AtendimentoIndividualResponse.AlunoResumo;
import aprimorar.agendamento.atendimentos_individuais.web.dto.calendario.AtendimentoIndividualCalendarioFiltroRequest;
import aprimorar.agendamento.atendimentos_individuais.web.dto.calendario.AtendimentoIndividualCalendarioResponse;
import aprimorar.agendamento.alunos.domain.Aluno;
import aprimorar.agendamento.alunos.service.AlunoService;
import aprimorar.agendamento.atendimentos_individuais.domain.AtendimentoIndividual;
import aprimorar.agendamento.atendimentos_individuais.domain.exception.AtendimentoIndividualConflitanteException;
import aprimorar.agendamento.atendimentos_individuais.domain.exception.AtendimentoIndividualDadosInvalidosException;
import aprimorar.agendamento.atendimentos_individuais.domain.exception.AtendimentoIndividualNaoEncontradoException;
import aprimorar.agendamento.colaboradores.domain.Colaborador;
import aprimorar.agendamento.colaboradores.service.ColaboradorService;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtendimentoIndividualService {

    private static final Logger log = LoggerFactory.getLogger(AtendimentoIndividualService.class);
    private final AtendimentoIndividualRepository atendimentoRepository;
    private final CobrancaAlunoApi cobrancaApi;
    private final RepasseApi repasseApi;
    private final AlunoService alunoService;
    private final ColaboradorService colaboradorService;

    public AtendimentoIndividualService(
        AtendimentoIndividualRepository atendimentoRepository,
        CobrancaAlunoApi cobrancaApi,
        RepasseApi repasseApi,
        AlunoService alunoService,
        ColaboradorService colaboradorService
    ) {
        this.atendimentoRepository = atendimentoRepository;
        this.cobrancaApi = cobrancaApi;
        this.repasseApi = repasseApi;
        this.alunoService = alunoService;
        this.colaboradorService = colaboradorService;
    }

    @Transactional(readOnly = true)
    public Page<AtendimentoIndividualResponse> buscarAtendimentos(Pageable pageable,AtendimentoIndividualFiltroRequest filtro) {

        Specification<AtendimentoIndividual> specification = AtendimentoIndividualSpecifications.paraAtendimentos(filtro);
        Page<AtendimentoIndividual> atendimentos = atendimentoRepository.findAll(specification, pageable);

        Set<Long> ids = idsDosAtendimentos(atendimentos.getContent());

        Map<Long, CobrancaAlunoResumo> cobrancas = cobrancaApi.buscarResumosPorOrigemIds(ids,TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL);
        Map<Long, RepasseParticularSummary> repasses = repasseApi.buscarSummariesPorAtendimentoIds(ids);

        return atendimentos.map(atendimento -> AtendimentoIndividualResponse.toDto(
            atendimento,
            cobrancaDoAtendimento(atendimento.getId(), cobrancas),
            repasseDoAtendimento(atendimento.getId(), repasses)
        ));
    }

    @Transactional(readOnly = true)
    public AtendimentoIndividualResponse buscarAtendimentoPorId(Long id) {
        AtendimentoIndividual atendimento = findAtendimentoOrThrow(id);

        CobrancaAlunoResumo cobranca = cobrancaApi.buscarResumoPorOrigemId(id, TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL)
            .orElseThrow(() -> new IllegalStateException("Cobrança não encontrada para o atendimento " + id));

        RepasseParticularSummary repasse = repasseApi.buscarSummaryPorAtendimentoId(id)
            .orElseThrow(() -> new IllegalStateException("Repasse não encontrado para o atendimento " + id));

        return AtendimentoIndividualResponse.toDto(
            atendimento,
            cobranca,
            repasse
        );
    }

    @Transactional(readOnly = true)
    public List<AtendimentoIndividualCalendarioResponse> buscarCalendario(
        AtendimentoIndividualCalendarioFiltroRequest filtro
    ) {
        List<AtendimentoIndividual> atendimentos = atendimentoRepository.findAll(
            AtendimentoIndividualSpecifications.paraCalendario(filtro),
            Sort.by(Sort.Direction.ASC, "dataHoraInicio")
        );

        return atendimentos.stream()
            .map(AtendimentoIndividualCalendarioResponse::toDto)
            .toList();
    }

    @Transactional
    public Long agendar(AtendimentoIndividualRequest request) {
        Aluno aluno = alunoService.findAlunoById(request.alunoId());

        if (!Boolean.TRUE.equals(aluno.getActive())) {
            throw new AtendimentoIndividualDadosInvalidosException(
                "Aluno informado está inativo"
            );
        }

        Colaborador colaborador = colaboradorService.findEntityById(request.colaboradorId())
            .orElseThrow(() -> new AtendimentoIndividualDadosInvalidosException(
                "Colaborador informado não encontrado"
            ));

        if (!Boolean.TRUE.equals(colaborador.getActive())) {
            throw new AtendimentoIndividualDadosInvalidosException(
                "Colaborador informado está inativo"
            );
        }

        validarDisponibilidade(
            aluno.getId(),
            colaborador.getId(),
            request.dataHoraInicio(),
            request.dataHoraFim(),
            null
        );

        AtendimentoIndividual atendimento = atendimentoRepository.save(
            new AtendimentoIndividual(
                request.dataHoraInicio(),
                request.dataHoraFim(),
                request.tipo(),
                aluno,
                colaborador
            )
        );

        cobrancaApi.criar(new CriarCobrancaAlunoCommand(
            atendimento.getId(),
            TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL,
            atendimento.getAluno().getId(),
            request.valorCobranca()
        ));
        repasseApi.criar(new CriarRepasseCommand(
            atendimento.getId(),
            atendimento.getColaborador().getId(),
            request.valorRepasse()
        ));

        log.info("Atendimento individual {} cadastrado com cobrança e repasse pendentes.", atendimento.getId());
        return atendimento.getId();
    }

    @Transactional
    public void update(Long id, AtendimentoIndividualRequest request) {
        AtendimentoIndividual atendimento = findAtendimentoOrThrow(id);
        atendimento.validarPodeEditar();

        Aluno aluno = alunoService.findAlunoById(request.alunoId());

        if (!Boolean.TRUE.equals(aluno.getActive())) {
            throw new AtendimentoIndividualDadosInvalidosException(
                "Aluno informado está inativo"
            );
        }

        Colaborador colaborador = colaboradorService.findEntityById(request.colaboradorId())
            .orElseThrow(() -> new AtendimentoIndividualDadosInvalidosException(
                "Colaborador informado não encontrado"
            ));

        if (!Boolean.TRUE.equals(colaborador.getActive())) {
            throw new AtendimentoIndividualDadosInvalidosException(
                "Colaborador informado está inativo"
            );
        }

        validarDisponibilidade(aluno.getId(), colaborador.getId(), request.dataHoraInicio(), request.dataHoraFim(), id);
        cobrancaApi.atualizar(new AtualizarCobrancaAlunoCommand(
            id,
            TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL,
            aluno.getId(),
            request.valorCobranca()
        ));

        repasseApi.atualizar(new AtualizarRepasseCommand(
            id,
            colaborador.getId(),
            request.valorRepasse()
        ));

        atendimento.update(request.dataHoraInicio(), request.dataHoraFim(), request.tipo(), aluno, colaborador);

        log.info("Atendimento individual {} atualizado.", id);
    }

    @Transactional
    public void realizar(Long id) {
        AtendimentoIndividual atendimento = findAtendimentoOrThrow(id);
        atendimento.realizar();
        log.info("Atendimento individual {} realizado.", id);
    }

    @Transactional
    public void cancelar(Long id) {
        AtendimentoIndividual atendimento = findAtendimentoOrThrow(id);
        atendimento.cancelar();
        cobrancaApi.cancelarPorOrigem(id, TipoOrigemCobrancaAluno.ATENDIMENTO_INDIVIDUAL);
        repasseApi.cancelarPorAtendimento(id);
        log.info("Atendimento individual {} cancelado.", id);
    }

    private Set<Long> idsDosAtendimentos(List<AtendimentoIndividual> atendimentos) {
        return atendimentos.stream()
            .map(AtendimentoIndividual::getId)
            .collect(Collectors.toUnmodifiableSet());
    }

    private CobrancaAlunoResumo cobrancaDoAtendimento(Long atendimentoId, Map<Long, CobrancaAlunoResumo> cobrancas) {
        CobrancaAlunoResumo cobranca = cobrancas.get(atendimentoId);
        if (cobranca == null) {
            throw new IllegalStateException("Cobrança não encontrada para o atendimento " + atendimentoId);
        }
        return cobranca;
    }

    private RepasseParticularSummary repasseDoAtendimento(
        Long atendimentoId,
        Map<Long, RepasseParticularSummary> repasses
    ) {
        RepasseParticularSummary repasse = repasses.get(atendimentoId);
        if (repasse == null) {
            throw new IllegalStateException("Repasse não encontrado para o atendimento " + atendimentoId);
        }
        return repasse;
    }

    private AtendimentoIndividual findAtendimentoOrThrow(Long id) {
        return atendimentoRepository.findById(id).orElseThrow(AtendimentoIndividualNaoEncontradoException::new);
    }

    private void validarDisponibilidade(
        UUID alunoId, UUID colaboradorId, LocalDateTime inicio, LocalDateTime fim, Long ignoredAtendimentoId
    ) {
        if (atendimentoRepository.alunoPossuiAtendimentoConflitante(
            alunoId, inicio, fim, ignoredAtendimentoId
        )) {
            throw new AtendimentoIndividualConflitanteException(
                "O aluno informado ja possui um atendimento no intervalo"
            );
        }
        if (atendimentoRepository.colaboradorPossuiAtendimentoConflitante(
            colaboradorId, inicio, fim, ignoredAtendimentoId
        )) {
            throw new AtendimentoIndividualConflitanteException(
                "O colaborador informado ja possui um atendimento no intervalo"
            );
        }
    }
}
