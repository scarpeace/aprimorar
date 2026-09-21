package aprimorar.financeiro.repasses_particular.service;

import aprimorar.financeiro.api.repasses_particular.AtualizarRepasseCommand;
import aprimorar.financeiro.api.repasses_particular.CriarRepasseCommand;
import aprimorar.financeiro.api.repasses_particular.RepasseApi;
import aprimorar.financeiro.api.repasses_particular.RepasseParticularSummary;
import aprimorar.financeiro.repasses_particular.domain.RepasseParticular;
import aprimorar.financeiro.repasses_particular.domain.enums.StatusRepasseParticular;
import aprimorar.financeiro.repasses_particular.domain.exception.RepasseParticularDadosInvalidosException;
import aprimorar.financeiro.repasses_particular.domain.exception.RepasseParticularNaoEncontradoException;
import aprimorar.financeiro.repasses_particular.repository.RepasseParticularRepository;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RepasseParticularService implements RepasseApi {

    private final RepasseParticularRepository repasseRepository;

    public RepasseParticularService(RepasseParticularRepository repasseRepository) {
        this.repasseRepository = repasseRepository;
    }

    @Override
    @Transactional
    public void criar(CriarRepasseCommand command) {

        if (command.atendimentoId() == null) {
            throw new RepasseParticularDadosInvalidosException("ID do atendimento é obrigatório");
        }

        if (command.colaboradorId() == null) {
            throw new RepasseParticularDadosInvalidosException("ID do colaborador é obrigatório");
        }

        if (command.valor() == null) {
            throw new RepasseParticularDadosInvalidosException("Valor é obrigatório");
        }

        repasseRepository.save(
            new RepasseParticular(
                command.atendimentoId(),
                command.colaboradorId(),
                command.valor()
            )
        );
    }

    @Override
    @Transactional
    public void atualizar(AtualizarRepasseCommand command) {

        if (command.atendimentoId() == null) {
            throw new RepasseParticularDadosInvalidosException("ID do atendimento é obrigatório");
        }

        if (command.colaboradorId() == null) {
            throw new RepasseParticularDadosInvalidosException("ID do colaborador é obrigatório");
        }

        if (command.valor() == null) {
            throw new RepasseParticularDadosInvalidosException("Valor é obrigatório");
        }

        RepasseParticular repasse = repasseRepository.findByAtendimentoIdForUpdate(command.atendimentoId())
            .orElseThrow(RepasseParticularNaoEncontradoException::new);

        repasse.atualizar(command.colaboradorId(), command.valor());
    }

    @Override
    @Transactional
    public void cancelarPorAtendimento(Long atendimentoId) {
        RepasseParticular repasse = repasseRepository.findByAtendimentoIdForUpdate(atendimentoId)
            .orElseThrow(RepasseParticularNaoEncontradoException::new);

        repasse.cancelar();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean possuiPendenciaPorColaboradorId(UUID colaboradorId) {
        return repasseRepository.existsByColaboradorIdAndStatus(colaboradorId,StatusRepasseParticular.PENDENTE);
    }

    @Override
    @Transactional(readOnly = true)
    public RepasseParticularSummary buscarSummaryPorAtendimentoId(
        Long atendimentoId
    ) {
        return repasseRepository.findByAtendimentoId(atendimentoId)
            .map(RepasseParticularService::toSummary)
            .orElseThrow(RepasseParticularNaoEncontradoException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, RepasseParticularSummary> buscarSummariesPorAtendimentoIds(
        Set<Long> atendimentoIds
    ) {
        if (atendimentoIds == null || atendimentoIds.isEmpty()) {
            return Map.of();
        }

        return repasseRepository.findAllByAtendimentoIdIn(atendimentoIds)
            .stream()
            .collect(Collectors.toUnmodifiableMap(
                RepasseParticular::getAtendimentoId,
                RepasseParticularService::toSummary
            ));
    }

    private static RepasseParticularSummary toSummary(RepasseParticular repasse) {
        return new RepasseParticularSummary(
            repasse.getId(),
            repasse.getAtendimentoId(),
            repasse.getValor(),
            repasse.statusAtual().name(),
            repasse.getCreatedAt(),
            repasse.getPagamento() == null ? null : repasse.getPagamento().getId()
        );
    }


}
