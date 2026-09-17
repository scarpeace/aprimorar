package aprimorar.instituicao.atendimentos.service;

import aprimorar.financeiro.api.cobrancas.AtualizarCobrancaCommand;
import aprimorar.financeiro.api.cobrancas.CobrancaApi;
import aprimorar.financeiro.api.cobrancas.CriarCobrancaCommand;
import aprimorar.financeiro.api.repasses.AtualizarRepasseCommand;
import aprimorar.financeiro.api.repasses.CriarRepasseCommand;
import aprimorar.financeiro.api.repasses.RepasseApi;
import aprimorar.financeiro.api.cobrancas.CobrancaResumo;
import aprimorar.financeiro.api.repasses.RepasseResumo;
import aprimorar.instituicao.atendimentos.repository.AtendimentoIndividualRepository;
import aprimorar.instituicao.atendimentos.repository.specifications.AtendimentoIndividualSpecifications;
import aprimorar.instituicao.atendimentos.web.dto.atendimento.AtendimentoIndividualFiltroRequest;
import aprimorar.instituicao.atendimentos.web.dto.atendimento.AtendimentoIndividualRequest;
import aprimorar.instituicao.atendimentos.web.dto.atendimento.AtendimentoIndividualResponse;
import aprimorar.instituicao.atendimentos.web.dto.calendario.AtendimentoIndividualCalendarioFiltroRequest;
import aprimorar.instituicao.atendimentos.web.dto.calendario.AtendimentoIndividualCalendarioResponse;
import aprimorar.instituicao.alunos.domain.AlunoEntity;
import aprimorar.instituicao.alunos.service.AlunoServiceImpl;
import aprimorar.instituicao.atendimentos.domain.AtendimentoIndividualEntity;
import aprimorar.instituicao.atendimentos.domain.exception.AtendimentoIndividualConflitanteException;
import aprimorar.instituicao.atendimentos.domain.exception.AtendimentoIndividualDadosInvalidosException;
import aprimorar.instituicao.atendimentos.domain.exception.AtendimentoIndividualNaoEncontradoException;
import aprimorar.instituicao.colaboradores.domain.ColaboradorEntity;
import aprimorar.instituicao.colaboradores.service.ColaboradorServiceImpl;

import java.math.BigDecimal;
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
    private final CobrancaApi cobrancaApi;
    private final RepasseApi repasseApi;
    private final AlunoServiceImpl alunoService;
    private final ColaboradorServiceImpl colaboradorService;

    public AtendimentoIndividualService(
        AtendimentoIndividualRepository atendimentoRepository,
        CobrancaApi cobrancaApi,
        RepasseApi repasseApi,
        AlunoServiceImpl alunoService,
        ColaboradorServiceImpl colaboradorService
    ) {
        this.atendimentoRepository = atendimentoRepository;
        this.cobrancaApi = cobrancaApi;
        this.repasseApi = repasseApi;
        this.alunoService = alunoService;
        this.colaboradorService = colaboradorService;
    }

    @Transactional(readOnly = true)
    public Page<AtendimentoIndividualResponse> buscarAtendimentos(
        Pageable pageable,
        AtendimentoIndividualFiltroRequest filtro
    ) {
        Specification<AtendimentoIndividualEntity> specification =
            AtendimentoIndividualSpecifications.paraAtendimentos(filtro);
        Page<AtendimentoIndividualEntity> atendimentos = atendimentoRepository.findAll(specification, pageable);
       
        Set<Long> ids = idsDosAtendimentos(atendimentos.getContent());

        Map<Long, CobrancaResumo> cobrancas = cobrancaApi.buscarResumosPorAtendimentoIds(ids);
        Map<Long, RepasseResumo> repasses = repasseApi.buscarResumosPorAtendimentoIds(ids);

        return atendimentos.map(atendimento -> AtendimentoIndividualResponse.from(
            atendimento,
            cobrancaDoAtendimento(atendimento.getId(), cobrancas),
            repasseDoAtendimento(atendimento.getId(), repasses)
        ));
    }

    @Transactional(readOnly = true)
    public AtendimentoIndividualResponse buscarAtendimentoPorId(Long id) {
        AtendimentoIndividualEntity atendimento = findAtendimentoOrThrow(id);
        Set<Long> ids = Set.of(atendimento.getId());
        Map<Long, CobrancaResumo> cobrancas = cobrancaApi.buscarResumosPorAtendimentoIds(ids);
        Map<Long, RepasseResumo> repasses = repasseApi.buscarResumosPorAtendimentoIds(ids);

        return AtendimentoIndividualResponse.from(
            atendimento,
            cobrancaDoAtendimento(id, cobrancas),
            repasseDoAtendimento(id, repasses)
        );
    }

    @Transactional(readOnly = true)
    public List<AtendimentoIndividualCalendarioResponse> buscarCalendario(
        AtendimentoIndividualCalendarioFiltroRequest filtro
    ) {
        List<AtendimentoIndividualEntity> atendimentos = atendimentoRepository.findAll(
            AtendimentoIndividualSpecifications.paraCalendario(filtro),
            Sort.by(Sort.Direction.ASC, "dataHoraInicio")
        );

        return atendimentos.stream()
            .map(AtendimentoIndividualCalendarioResponse::from)
            .toList();
    }

    @Transactional
    public Long agendar(AtendimentoIndividualRequest request) {
        AlunoEntity aluno = buscarAluno(request.alunoId());
        ColaboradorEntity colaborador = buscarColaborador(request.colaboradorId());

        validarDisponibilidade(
            aluno.getId(),
            colaborador.getId(),
            request.dataHoraInicio(),
            request.dataHoraFim(),
            null
        );

        validarValores(request.valorCobranca(), request.valorRepasse());

        AtendimentoIndividualEntity atendimento = atendimentoRepository.save(
            new AtendimentoIndividualEntity(
                request.dataHoraInicio(),
                request.dataHoraFim(),
                request.tipo(),
                aluno,
                colaborador
            )
        );

        cobrancaApi.criar(new CriarCobrancaCommand(
            atendimento.getId(),
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
        AtendimentoIndividualEntity atendimento = findAtendimentoOrThrow(id);
        atendimento.validarPodeEditar();

        AlunoEntity aluno = buscarAluno(request.alunoId());
        ColaboradorEntity colaborador = buscarColaborador(request.colaboradorId());

        validarDisponibilidade(
            aluno.getId(), colaborador.getId(), request.dataHoraInicio(), request.dataHoraFim(), id
        );

        validarValores(request.valorCobranca(), request.valorRepasse());

        cobrancaApi.atualizar(new AtualizarCobrancaCommand(
            id,
            aluno.getId(),
            request.valorCobranca()
        ));
        repasseApi.atualizar(new AtualizarRepasseCommand(
            id,
            colaborador.getId(),
            request.valorRepasse()
        ));

        atendimento.update(
            request.dataHoraInicio(), request.dataHoraFim(), request.tipo(), aluno, colaborador
        );

        log.info("Atendimento individual {} atualizado.", id);
    }

    @Transactional
    public void realizar(Long id) {
        AtendimentoIndividualEntity atendimento = findAtendimentoOrThrow(id);
        atendimento.realizar();
        log.info("Atendimento individual {} realizado.", id);
    }

    @Transactional
    public void cancelar(Long id) {
        AtendimentoIndividualEntity atendimento = findAtendimentoOrThrow(id);
        atendimento.cancelar();
        cobrancaApi.cancelarPorAtendimento(id);
        repasseApi.cancelarPorAtendimento(id);
        log.info("Atendimento individual {} cancelado.", id);
    }



    private Set<Long> idsDosAtendimentos(List<AtendimentoIndividualEntity> atendimentos) {
        return atendimentos.stream()
            .map(AtendimentoIndividualEntity::getId)
            .collect(Collectors.toUnmodifiableSet());
    }

    private CobrancaResumo cobrancaDoAtendimento(Long atendimentoId, Map<Long, CobrancaResumo> cobrancas) {
        CobrancaResumo cobranca = cobrancas.get(atendimentoId);
        if (cobranca == null) {
            throw new IllegalStateException("Cobrança não encontrada para o atendimento " + atendimentoId);
        }
        return cobranca;
    }

    private RepasseResumo repasseDoAtendimento(Long atendimentoId, Map<Long, RepasseResumo> repasses) {
        RepasseResumo repasse = repasses.get(atendimentoId);
        if (repasse == null) {
            throw new IllegalStateException("Repasse não encontrado para o atendimento " + atendimentoId);
        }
        return repasse;
    }

    private AtendimentoIndividualEntity findAtendimentoOrThrow(Long id) {
        return atendimentoRepository.findById(id).orElseThrow(AtendimentoIndividualNaoEncontradoException::new);
    }

    private AlunoEntity buscarAluno(UUID alunoId) {
        AlunoEntity aluno = alunoService.findEntityById(alunoId)
            .orElseThrow(() -> new AtendimentoIndividualDadosInvalidosException("Aluno informado não encontrado"));

        if (!Boolean.TRUE.equals(aluno.getActive())) {
            throw new AtendimentoIndividualDadosInvalidosException("Aluno informado está inativo");
        }

        return aluno;
    }

    private ColaboradorEntity buscarColaborador(UUID colaboradorId) {
        ColaboradorEntity colaborador = colaboradorService.findEntityById(colaboradorId)
            .orElseThrow(() -> new AtendimentoIndividualDadosInvalidosException("Colaborador informado não encontrado"));

        if (!Boolean.TRUE.equals(colaborador.getActive())) {
            throw new AtendimentoIndividualDadosInvalidosException("Colaborador informado está inativo");
        }

        return colaborador;
    }

    private void validarValores(BigDecimal valorCobranca, BigDecimal valorRepasse) {
        if (valorCobranca.compareTo(valorRepasse) < 0) {
            throw new AtendimentoIndividualDadosInvalidosException(
                "O valor da cobrança não pode ser menor que o repasse"
            );
        }
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
