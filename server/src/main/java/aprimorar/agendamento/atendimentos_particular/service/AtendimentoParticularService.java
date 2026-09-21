package aprimorar.agendamento.atendimentos_particular.service;

import aprimorar.agendamento.alunos.domain.Aluno;
import aprimorar.agendamento.alunos.service.AlunoService;
import aprimorar.agendamento.atendimentos_particular.domain.AtendimentoParticular;
import aprimorar.agendamento.atendimentos_particular.domain.exception.AtendimentoParticularConflitanteException;
import aprimorar.agendamento.atendimentos_particular.domain.exception.AtendimentoParticularDadosInvalidosException;
import aprimorar.agendamento.atendimentos_particular.domain.exception.AtendimentoParticularNaoEncontradoException;
import aprimorar.agendamento.atendimentos_particular.repository.AtendimentoParticularRepository;
import aprimorar.agendamento.atendimentos_particular.repository.AtendimentoSpecifications;
import aprimorar.agendamento.atendimentos_particular.web.dto.atendimento.AgendarAtendimentoParticularRequest;
import aprimorar.agendamento.atendimentos_particular.web.dto.atendimento.AtualizarAtendimentoParticularRequest;
import aprimorar.agendamento.atendimentos_particular.web.dto.atendimento.AtendimentoParticularFiltroRequest;
import aprimorar.agendamento.atendimentos_particular.web.dto.atendimento.AtendimentoParticularResponse;
import aprimorar.agendamento.colaboradores.domain.Colaborador;
import aprimorar.agendamento.colaboradores.service.ColaboradorService;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.api.AtualizarCobrancaParticularCommand;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.api.CobrancaParticularAPI;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.api.CobrancaParticularSummary;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.api.CriarCobrancaParticularCommand;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.api.AtualizarRepasseCommand;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.api.CriarRepasseCommand;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.api.RepasseAPI;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.api.RepasseParticularSummary;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtendimentoParticularService {

    private final AtendimentoParticularRepository atendimentoRepository;
    private final CobrancaParticularAPI cobrancaParticularApi;
    private final RepasseAPI repasseParticularApi;
    private final AlunoService alunoService;
    private final ColaboradorService colaboradorService;

    public AtendimentoParticularService(
        AtendimentoParticularRepository atendimentoRepository,
        CobrancaParticularAPI cobrancaApi,
        RepasseAPI repasseApi,
        AlunoService alunoService,
        ColaboradorService colaboradorService
    ) {
        this.atendimentoRepository = atendimentoRepository;
        this.cobrancaParticularApi = cobrancaApi;
        this.repasseParticularApi = repasseApi;
        this.alunoService = alunoService;
        this.colaboradorService = colaboradorService;
    }

    @Transactional(readOnly = true)
    public Page<AtendimentoParticularResponse> buscarAtendimentos(
        AtendimentoParticularFiltroRequest filtro,
        Pageable pageable
    ) {
        Specification<AtendimentoParticular> specification = AtendimentoSpecifications.comFiltros(filtro);
        Page<AtendimentoParticular> atendimentos = atendimentoRepository.findAll(specification, pageable);

        Set<Long> atendimentoIds = atendimentos.getContent().stream()
            .map(atendimento -> atendimento.getId())
            .collect(Collectors.toUnmodifiableSet());

        Map<Long, CobrancaParticularSummary> cobrancas =
            cobrancaParticularApi.buscarSummariesPorAtendimentoIds(atendimentoIds);
        Map<Long, RepasseParticularSummary> repasses =
            repasseParticularApi.buscarSummariesPorAtendimentoIds(atendimentoIds);

        return atendimentos.map(atendimento ->
            AtendimentoParticularResponse.toDto(
                atendimento,
                cobrancas.get(atendimento.getId()),
                repasses.get(atendimento.getId())
            )
        );
    }

    @Transactional(readOnly = true)
    public AtendimentoParticularResponse buscarAtendimentoPorId(Long atendimentoId) {
        AtendimentoParticular atendimento = findAtendimentoOrThrow(atendimentoId);
        CobrancaParticularSummary cobranca = cobrancaParticularApi.buscarSummaryPorAtendimentoId(atendimentoId);
        RepasseParticularSummary repasse = repasseParticularApi.buscarSummaryPorAtendimentoId(atendimentoId);

        return AtendimentoParticularResponse.toDto(
            atendimento,
            cobranca,
            repasse
        );
    }

    @Transactional
    public Long agendar(
        AgendarAtendimentoParticularRequest dto
    ) {

        Aluno aluno = alunoService.findAlunoOrThrow(dto.alunoId());
        if (!Boolean.TRUE.equals(aluno.getActive())) {
            throw new AtendimentoParticularDadosInvalidosException(
                "Aluno informado não está ativo"
            );
        }

        Colaborador colaborador = colaboradorService.findColaboradorOrThrow(dto.colaboradorId());
        if (!Boolean.TRUE.equals(colaborador.getActive())) {
            throw new AtendimentoParticularDadosInvalidosException(
                "Colaborador informado não está ativo"
            );
        }

        validarConflitoHorario(
            aluno.getId(),
            colaborador.getId(),
            dto.dataHoraInicio(),
            dto.dataHoraFim(),
            null
        );

        AtendimentoParticular novoAtendimento = atendimentoRepository.save(
            new AtendimentoParticular(dto.dataHoraInicio(), dto.dataHoraFim(), dto.tipo(), aluno, colaborador)
        );

        cobrancaParticularApi.criar(new CriarCobrancaParticularCommand(
            novoAtendimento.getId(),
            aluno.getId(),
            dto.valorCobranca()
        ));

        repasseParticularApi.criar(new CriarRepasseCommand(
            novoAtendimento.getId(),
            colaborador.getId(),
            dto.valorRepasse()
        ));

        return novoAtendimento.getId();
    }

    @Transactional
    public void atualizar(
        Long atendimentoId,
        AtualizarAtendimentoParticularRequest dto
    ) {
        AtendimentoParticular atendimento = findAtendimentoOrThrow(atendimentoId);

        Aluno aluno = alunoService.findAlunoOrThrow(dto.alunoId());
        if (!Boolean.TRUE.equals(aluno.getActive())) {
            throw new AtendimentoParticularDadosInvalidosException(
                "Aluno informado não está ativo"
            );
        }

        Colaborador colaborador = colaboradorService.findColaboradorOrThrow(dto.colaboradorId());
        if (!Boolean.TRUE.equals(colaborador.getActive())) {
            throw new AtendimentoParticularDadosInvalidosException(
                "Colaborador informado não está ativo"
            );
        }

        CobrancaParticularSummary cobranca = cobrancaParticularApi.buscarSummaryPorAtendimentoId(atendimentoId);
        if(cobranca.recebimentoId() != null) {
            throw new AtendimentoParticularDadosInvalidosException("Não é possível alterar um atendimento já pago");
        }

        RepasseParticularSummary repasse = repasseParticularApi.buscarSummaryPorAtendimentoId(atendimentoId);
        if(repasse.pagamentoId() != null) {
            throw new AtendimentoParticularDadosInvalidosException("Não é possível alterar um atendimento já pago");
        }

        validarConflitoHorario(
            aluno.getId(),
            colaborador.getId(),
            dto.dataHoraInicio(),
            dto.dataHoraFim(),
            atendimentoId
        );

        atendimento.atualizar(
            dto.dataHoraInicio(),
            dto.dataHoraFim(),
            dto.tipo(),
            aluno,
            colaborador
        );

        cobrancaParticularApi.atualizar(new AtualizarCobrancaParticularCommand(
            atendimentoId,
            aluno.getId(),
            dto.valorCobranca()
        ));
        repasseParticularApi.atualizar(new AtualizarRepasseCommand(
            atendimentoId,
            colaborador.getId(),
            dto.valorRepasse()
        ));
    }

    @Transactional
    public void realizar(Long atendimentoId) {
        AtendimentoParticular atendimento = findAtendimentoOrThrow(atendimentoId);
        atendimento.realizar();
    }

    @Transactional
    public void cancelar(Long atendimentoId) {
        AtendimentoParticular atendimento = findAtendimentoOrThrow(atendimentoId);

        CobrancaParticularSummary cobranca = cobrancaParticularApi.buscarSummaryPorAtendimentoId(atendimentoId);
        if(cobranca.recebimentoId() != null) {
            throw new AtendimentoParticularDadosInvalidosException("Não é possível cancelar um atendimento com recebimento registrado");
        }

        RepasseParticularSummary repasse = repasseParticularApi.buscarSummaryPorAtendimentoId(atendimentoId);
        if(repasse.pagamentoId() != null) {
            throw new AtendimentoParticularDadosInvalidosException("Não é possível cancelar um atendimento com pagamento registrado");
        }

        atendimento.cancelar();
        cobrancaParticularApi.cancelarPorAtendimento(atendimentoId);
        repasseParticularApi.cancelarPorAtendimento(atendimentoId);
    }

    private AtendimentoParticular findAtendimentoOrThrow(Long atendimentoId) {
        return atendimentoRepository.findById(atendimentoId)
            .orElseThrow(AtendimentoParticularNaoEncontradoException::new);
    }

    private void validarConflitoHorario(
        UUID alunoId,
        UUID colaboradorId,
        LocalDateTime inicio,
        LocalDateTime fim,
        Long ignoredAtendimentoId
    ) {
        if (atendimentoRepository.alunoPossuiAtendimentoConflitante(alunoId,inicio,fim,ignoredAtendimentoId)) {
            throw new AtendimentoParticularConflitanteException("O aluno informado já possui um atendimento no intervalo");
        }

        if (atendimentoRepository.colaboradorPossuiAtendimentoConflitante(colaboradorId,inicio,fim,ignoredAtendimentoId)) {
            throw new AtendimentoParticularConflitanteException("O colaborador informado já possui um atendimento no intervalo");
        }
    }
}
