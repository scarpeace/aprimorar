package aprimorar.financeiro.recebimentos_alunos.api;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import aprimorar.common.utils.ExceptionUtils;
import aprimorar.financeiro.recebimentos_alunos.api.commands.AtualizarCobrancaCommandApi;
import aprimorar.financeiro.recebimentos_alunos.api.commands.CriarCobrancaCommandApi;
import aprimorar.financeiro.recebimentos_alunos.api.queries.CobrancaSummary;
import aprimorar.financeiro.recebimentos_alunos.domain.Cobranca;
import aprimorar.financeiro.recebimentos_alunos.domain.enums.StatusCobranca;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.CobrancaJaExistenteException;
import aprimorar.financeiro.recebimentos_alunos.infrastructure.CobrancaRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import aprimorar.financeiro.recebimentos_alunos.domain.exception.CobrancaDadosInvalidosException;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.CobrancaNaoEncontradaException;

@Service
class RecebimentosApiImpl implements RecebimentosApi {

    private final CobrancaRepository cobrancaRepository;

    RecebimentosApiImpl(CobrancaRepository cobrancaRepository) {
        this.cobrancaRepository = cobrancaRepository;
    }

    @Override
    @Transactional
    public void criarCobranca(CriarCobrancaCommandApi command) {
        if (!CriarCobrancaCommandApi.validate(command)) {
            throw new CobrancaDadosInvalidosException(
                "ID do atendimento, ID do aluno e valor são obrigatórios"
            );
        }

        try {
            cobrancaRepository.saveAndFlush(
                new Cobranca(
                    command.atendimentoId(),
                    command.alunoId(),
                    command.valor()
                )
            );
        } catch (DataIntegrityViolationException ex) {
            throw traduzirViolacaoDaCobranca(ex);
        }
    }

    @Override
    @Transactional
    public void atualizarCobranca(AtualizarCobrancaCommandApi command) {
        if (!AtualizarCobrancaCommandApi.validate(command)) {
            throw new CobrancaDadosInvalidosException(
                "ID do atendimento, ID do aluno e valor são obrigatórios"
            );
        }

        Cobranca cobranca = cobrancaRepository
            .findByAtendimentoIdForUpdate(command.atendimentoId())
            .orElseThrow(CobrancaNaoEncontradaException::new);

        cobranca.atualizar(command.alunoId(), command.valor());
    }

    @Override
    @Transactional
    public void cancelarCobranca(Long atendimentoId) {
        Cobranca cobranca = cobrancaRepository
            .findByAtendimentoIdForUpdate(atendimentoId)
            .orElseThrow(CobrancaNaoEncontradaException::new);

        cobranca.cancelar();
    }

    @Override
    @Transactional
    public boolean possuiCobrancaPendente(UUID alunoId) {
        return cobrancaRepository.existsByAlunoIdAndStatus(
            alunoId,
            StatusCobranca.PENDENTE
        );
    }

    @Override
    @Transactional
    public CobrancaSummary getCobrancaSummaryPorAtendimento(Long atendimentoId) {
        return cobrancaRepository.findByAtendimentoId(atendimentoId)
            .map(CobrancaSummary::toSummary)
            .orElseThrow(CobrancaNaoEncontradaException::new);
    }

    @Override
    @Transactional
    public Map<Long, CobrancaSummary> getCobrancasSummariesPorAtendimentos(
        Set<Long> atendimentoIds
    ) {
        if (atendimentoIds == null || atendimentoIds.isEmpty()) {
            return Map.of();
        }

        return cobrancaRepository.findAllByAtendimentoIdIn(atendimentoIds)
            .stream()
            .collect(Collectors.toUnmodifiableMap(
                Cobranca::getAtendimentoId,
                CobrancaSummary::toSummary
            ));
    }

    private RuntimeException traduzirViolacaoDaCobranca(
        DataIntegrityViolationException ex
    ) {
        String constraint = ExceptionUtils.findConstraintName(ex);

        return switch (constraint == null ? "" : constraint) {
            case "uk_cobrancas_atendimento" -> new CobrancaJaExistenteException();
            case "ck_cobrancas_valor" -> new CobrancaDadosInvalidosException(
                "O valor da cobrança não pode ser negativo."
            );
            default -> ex;
        };
    }
}
