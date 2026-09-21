package aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.api;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface RepasseAPI {

    void criar(CriarRepasseCommand command);

    void atualizar(AtualizarRepasseCommand command);

    void cancelarPorAtendimento(Long atendimentoId);

    boolean possuiPendenciaPorColaboradorId(UUID colaboradorId);

    RepasseParticularSummary buscarSummaryPorAtendimentoId(Long atendimentoId);

    Map<Long, RepasseParticularSummary> buscarSummariesPorAtendimentoIds(
        Set<Long> atendimentoIds
    );
}
