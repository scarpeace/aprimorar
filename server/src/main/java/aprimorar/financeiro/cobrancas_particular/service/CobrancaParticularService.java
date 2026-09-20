package aprimorar.financeiro.cobrancas_particular.service;

import aprimorar.financeiro.api.cobrancas_particular.AtualizarCobrancaParticularCommand;
import aprimorar.financeiro.api.cobrancas_particular.CobrancaParticularApi;
import aprimorar.financeiro.api.cobrancas_particular.CobrancaParticularSummary;
import aprimorar.financeiro.api.cobrancas_particular.CriarCobrancaParticularCommand;
import aprimorar.financeiro.cobrancas_particular.domain.CobrancaParticular;
import aprimorar.financeiro.cobrancas_particular.domain.enums.StatusCobrancaParticular;
import aprimorar.financeiro.cobrancas_particular.domain.exception.CobrancaParticularDadosInvalidosException;
import aprimorar.financeiro.cobrancas_particular.domain.exception.CobrancaParticularNaoEncontradaException;
import aprimorar.financeiro.cobrancas_particular.repository.CobrancaParticularRepository;
import aprimorar.financeiro.cobrancas_particular.repository.CobrancaParticularSpecifications;
import aprimorar.financeiro.cobrancas_particular.web.dto.CobrancaParticularFiltroRequest;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CobrancaParticularService implements CobrancaParticularApi {

    private final CobrancaParticularRepository cobrancaRepository;

    public CobrancaParticularService(CobrancaParticularRepository cobrancaRepository) {
        this.cobrancaRepository = cobrancaRepository;
    }

    @Override
    @Transactional
    public void criar(CriarCobrancaParticularCommand command) {
        validarDados(command);

        cobrancaRepository.save(
            new CobrancaParticular(
                command.atendimentoId(),
                command.alunoId(),
                command.valor()
            )
        );
    }

    @Override
    @Transactional
    public void atualizar(AtualizarCobrancaParticularCommand command) {
        validarDados(command);

        CobrancaParticular cobranca = cobrancaRepository
            .findByAtendimentoIdForUpdate(command.atendimentoId())
            .orElseThrow(CobrancaParticularNaoEncontradaException::new);

        cobranca.atualizar(command.alunoId(), command.valor());
    }

    @Override
    @Transactional
    public void cancelarPorAtendimento(Long atendimentoId) {
        if (atendimentoId == null) {
            throw new CobrancaParticularDadosInvalidosException(
                "ID do atendimento é obrigatório"
            );
        }

        CobrancaParticular cobranca = cobrancaRepository
            .findByAtendimentoIdForUpdate(atendimentoId)
            .orElseThrow(CobrancaParticularNaoEncontradaException::new);

        cobranca.cancelar();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean possuiPendenciaPorAlunoId(UUID alunoId) {
        return cobrancaRepository.existsByAlunoIdAndStatus(
            alunoId,
            StatusCobrancaParticular.PENDENTE
        );
    }

    @Transactional(readOnly = true)
    public Page<CobrancaParticular> buscarCobrancas(
        CobrancaParticularFiltroRequest filtro,
        Pageable pageable
    ) {
        return cobrancaRepository.findAll(
            CobrancaParticularSpecifications.comFiltros(filtro),
            pageable
        );
    }

    @Transactional(readOnly = true)
    public CobrancaParticular buscarCobrancaPorId(Long cobrancaId) {
        return cobrancaRepository.findById(cobrancaId)
            .orElseThrow(CobrancaParticularNaoEncontradaException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CobrancaParticularSummary> buscarSummaryPorAtendimentoId(
        Long atendimentoId
    ) {
        return cobrancaRepository.findByAtendimentoId(atendimentoId)
            .map(CobrancaParticularService::toSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, CobrancaParticularSummary> buscarSummariesPorAtendimentoIds(
        Set<Long> atendimentoIds
    ) {
        if (atendimentoIds == null || atendimentoIds.isEmpty()) {
            return Map.of();
        }

        return cobrancaRepository.findAllByAtendimentoIdIn(atendimentoIds)
            .stream()
            .collect(Collectors.toUnmodifiableMap(
                CobrancaParticular::getAtendimentoId,
                CobrancaParticularService::toSummary
            ));
    }

    private static void validarDados(CriarCobrancaParticularCommand command) {
        if (command == null) {
            throw new CobrancaParticularDadosInvalidosException(
                "Dados da cobrança são obrigatórios"
            );
        }

        validarDados(command.atendimentoId(), command.alunoId(), command.valor());
    }

    private static void validarDados(AtualizarCobrancaParticularCommand command) {
        if (command == null) {
            throw new CobrancaParticularDadosInvalidosException(
                "Dados da cobrança são obrigatórios"
            );
        }

        validarDados(command.atendimentoId(), command.alunoId(), command.valor());
    }

    private static void validarDados(Long atendimentoId, UUID alunoId, BigDecimal valor) {
        if (atendimentoId == null) {
            throw new CobrancaParticularDadosInvalidosException(
                "ID do atendimento é obrigatório"
            );
        }

        if (alunoId == null) {
            throw new CobrancaParticularDadosInvalidosException(
                "ID do aluno é obrigatório"
            );
        }

        if (valor == null) {
            throw new CobrancaParticularDadosInvalidosException(
                "Valor é obrigatório"
            );
        }
    }

    private static CobrancaParticularSummary toSummary(CobrancaParticular cobranca) {
        return new CobrancaParticularSummary(
            cobranca.getId(),
            cobranca.getAtendimentoId(),
            cobranca.getValor(),
            cobranca.statusAtual().name(),
            cobranca.getCreatedAt(),
            cobranca.getRecebimento() == null ? null : cobranca.getRecebimento().getId()
        );
    }
}
