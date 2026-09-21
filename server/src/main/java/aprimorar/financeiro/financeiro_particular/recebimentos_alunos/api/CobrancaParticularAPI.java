package aprimorar.financeiro.financeiro_particular.recebimentos_alunos.api;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface CobrancaParticularAPI {

    void criar(CriarCobrancaParticularCommand command);

    void atualizar(AtualizarCobrancaParticularCommand command);

    void cancelarPorAtendimento(Long atendimentoId);

    boolean possuiPendenciaPorAlunoId(UUID alunoId);

    CobrancaParticularSummary buscarSummaryPorAtendimentoId(Long atendimentoId);

    Map<Long, CobrancaParticularSummary> buscarSummariesPorAtendimentoIds(
        Set<Long> atendimentoIds
    );
}
