package aprimorar.financeiro.api.repasses_particular;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface RepasseApi {

    void criar(CriarRepasseCommand command);

    void atualizar(AtualizarRepasseCommand command);

    void cancelarPorAtendimento(Long atendimentoId);

    boolean possuiPendenciaPorColaboradorId(UUID colaboradorId);

    Optional<RepasseParticularSummary> buscarSummaryPorAtendimentoId(Long atendimentoId);

    Map<Long, RepasseParticularSummary> buscarSummariesPorAtendimentoIds(
        Set<Long> atendimentoIds
    );
}
