package aprimorar.agendamento.atendimentos_individuais.application;

import aprimorar.agendamento.alunos.domain.Aluno;
import aprimorar.agendamento.alunos.application.AlunoService;
import aprimorar.agendamento.atendimentos_individuais.domain.AtendimentoIndividual;
import aprimorar.agendamento.atendimentos_individuais.domain.exception.AtendimentoIndividualConflitanteException;
import aprimorar.agendamento.atendimentos_individuais.domain.exception.AtendimentoIndividualDadosInvalidosException;
import aprimorar.agendamento.atendimentos_individuais.domain.exception.AtendimentoIndividualNaoEncontradoException;
import aprimorar.agendamento.atendimentos_individuais.infrastructure.AtendimentoIndividualRepository;
import aprimorar.agendamento.atendimentos_individuais.infrastructure.AtendimentoIndividualSpecifications;
import aprimorar.agendamento.atendimentos_individuais.web.dto.AgendarAtendimentoIndividualRequest;
import aprimorar.agendamento.atendimentos_individuais.web.dto.AtualizarAtendimentoIndividualRequest;
import aprimorar.agendamento.atendimentos_individuais.web.dto.AtendimentoIndividualFiltroRequest;
import aprimorar.agendamento.atendimentos_individuais.web.dto.AtendimentoIndividualResponse;
import aprimorar.agendamento.colaboradores.domain.Colaborador;
import aprimorar.agendamento.colaboradores.application.ColaboradorService;
import aprimorar.financeiro.recebimentos_alunos.api.RecebimentosApi;
import aprimorar.financeiro.recebimentos_alunos.api.commands.AtualizarCobrancaCommandApi;
import aprimorar.financeiro.recebimentos_alunos.api.commands.CriarCobrancaCommandApi;
import aprimorar.financeiro.recebimentos_alunos.api.queries.CobrancaSummary;
import aprimorar.financeiro.repasses_colaboradores.api.PagamentosApi;
import aprimorar.financeiro.repasses_colaboradores.api.commands.AtualizarRepasseCommandApi;
import aprimorar.financeiro.repasses_colaboradores.api.commands.CriarRepasseCommandApi;
import aprimorar.financeiro.repasses_colaboradores.api.queries.RepasseSummary;

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
public class AtendimentoIndividualService {

    private final AtendimentoIndividualRepository atendimentoRepository;
    private final RecebimentosApi cobrancaApi;
    private final PagamentosApi repasseApi;
    private final AlunoService alunoService;
    private final ColaboradorService colaboradorService;

    public AtendimentoIndividualService(
        AtendimentoIndividualRepository atendimentoRepository,
        RecebimentosApi cobrancaApi,
        PagamentosApi repasseApi,
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
    public Page<AtendimentoIndividualResponse> buscarAtendimentos(
        AtendimentoIndividualFiltroRequest filtro,
        Pageable pageable
    ) {
        Specification<AtendimentoIndividual> specification = AtendimentoIndividualSpecifications.comFiltros(filtro);
        Page<AtendimentoIndividual> atendimentos = atendimentoRepository.findAll(specification, pageable);

        Set<Long> atendimentoIds = atendimentos.getContent().stream()
            .map(atendimento -> atendimento.getId())
            .collect(Collectors.toUnmodifiableSet());

        Map<Long, CobrancaSummary> cobrancas =
            cobrancaApi.getCobrancasSummariesPorAtendimentos(atendimentoIds);
        Map<Long, RepasseSummary> repasses =
            repasseApi.getRepassesSummariesPorAtendimentos(atendimentoIds);

        return atendimentos.map(atendimento ->
            AtendimentoIndividualResponse.toDto(
                atendimento,
                cobrancas.get(atendimento.getId()),
                repasses.get(atendimento.getId())
            )
        );
    }

    @Transactional(readOnly = true)
    public AtendimentoIndividualResponse buscarAtendimentoPorId(Long atendimentoId) {
        AtendimentoIndividual atendimento = findAtendimentoOrThrow(atendimentoId);
        CobrancaSummary cobranca = cobrancaApi.getCobrancaSummaryPorAtendimento(atendimentoId);
        RepasseSummary repasse = repasseApi.getRepasseSummaryPorAtendimento(atendimentoId);

        return AtendimentoIndividualResponse.toDto(
            atendimento,
            cobranca,
            repasse
        );
    }

    @Transactional
    public Long agendar(
        AgendarAtendimentoIndividualRequest dto
    ) {

        Aluno aluno = alunoService.findAlunoOrThrow(dto.alunoId());
        if (!Boolean.TRUE.equals(aluno.getActive())) {
            throw new AtendimentoIndividualDadosInvalidosException(
                "Aluno informado não está ativo"
            );
        }

        Colaborador colaborador = colaboradorService.findColaboradorOrThrow(dto.colaboradorId());
        if (!Boolean.TRUE.equals(colaborador.getActive())) {
            throw new AtendimentoIndividualDadosInvalidosException(
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

        AtendimentoIndividual novoAtendimento = atendimentoRepository.save(
            new AtendimentoIndividual(dto.dataHoraInicio(), dto.dataHoraFim(), dto.tipo(), aluno, colaborador)
        );

        cobrancaApi.criarCobranca(new CriarCobrancaCommandApi(
            novoAtendimento.getId(),
            aluno.getId(),
            dto.valorCobranca()
        ));

        repasseApi.criarRepasse(new CriarRepasseCommandApi(
            novoAtendimento.getId(),
            colaborador.getId(),
            dto.valorRepasse()
        ));

        return novoAtendimento.getId();
    }

    @Transactional
    public void atualizar(
        Long atendimentoId,
        AtualizarAtendimentoIndividualRequest dto
    ) {
        AtendimentoIndividual atendimento = findAtendimentoOrThrow(atendimentoId);

        Aluno aluno = alunoService.findAlunoOrThrow(dto.alunoId());
        if (!Boolean.TRUE.equals(aluno.getActive())) {
            throw new AtendimentoIndividualDadosInvalidosException(
                "Aluno informado não está ativo"
            );
        }

        Colaborador colaborador = colaboradorService.findColaboradorOrThrow(dto.colaboradorId());
        if (!Boolean.TRUE.equals(colaborador.getActive())) {
            throw new AtendimentoIndividualDadosInvalidosException(
                "Colaborador informado não está ativo"
            );
        }

        CobrancaSummary cobranca = cobrancaApi.getCobrancaSummaryPorAtendimento(atendimentoId);
        if(cobranca.recebimentoId() != null) {
            throw new AtendimentoIndividualDadosInvalidosException("Não é possível alterar um atendimento já pago");
        }

        RepasseSummary repasse = repasseApi.getRepasseSummaryPorAtendimento(atendimentoId);
        if(repasse.pagamentoId() != null) {
            throw new AtendimentoIndividualDadosInvalidosException("Não é possível alterar um atendimento já pago");
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

        cobrancaApi.atualizarCobranca(new AtualizarCobrancaCommandApi(
            atendimentoId,
            aluno.getId(),
            dto.valorCobranca()
        ));
        repasseApi.atualizarRepasse(new AtualizarRepasseCommandApi(
            atendimentoId,
            colaborador.getId(),
            dto.valorRepasse()
        ));
    }

    @Transactional
    public void realizar(Long atendimentoId) {
        AtendimentoIndividual atendimento = findAtendimentoOrThrow(atendimentoId);
        atendimento.realizar();
    }

    @Transactional
    public void cancelar(Long atendimentoId) {
        AtendimentoIndividual atendimento = findAtendimentoOrThrow(atendimentoId);

        CobrancaSummary cobranca = cobrancaApi.getCobrancaSummaryPorAtendimento(atendimentoId);
        if(cobranca.recebimentoId() != null) {
            throw new AtendimentoIndividualDadosInvalidosException("Não é possível cancelar um atendimento com recebimento registrado");
        }

        RepasseSummary repasse = repasseApi.getRepasseSummaryPorAtendimento(atendimentoId);
        if(repasse.pagamentoId() != null) {
            throw new AtendimentoIndividualDadosInvalidosException("Não é possível cancelar um atendimento com pagamento registrado");
        }

        atendimento.cancelar();
        cobrancaApi.cancelarCobranca(atendimentoId);
        repasseApi.cancelarRepasse(atendimentoId);
    }

    private AtendimentoIndividual findAtendimentoOrThrow(Long atendimentoId) {
        return atendimentoRepository.findById(atendimentoId)
            .orElseThrow(AtendimentoIndividualNaoEncontradoException::new);
    }

    private void validarConflitoHorario(
        UUID alunoId,
        UUID colaboradorId,
        LocalDateTime inicio,
        LocalDateTime fim,
        Long ignoredAtendimentoId
    ) {
        if (atendimentoRepository.alunoPossuiAtendimentoConflitante(alunoId,inicio,fim,ignoredAtendimentoId)) {
            throw new AtendimentoIndividualConflitanteException("O aluno informado já possui um atendimento no intervalo");
        }

        if (atendimentoRepository.colaboradorPossuiAtendimentoConflitante(colaboradorId,inicio,fim,ignoredAtendimentoId)) {
            throw new AtendimentoIndividualConflitanteException("O colaborador informado já possui um atendimento no intervalo");
        }
    }
}
