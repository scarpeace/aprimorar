package aprimorar.financeiro.api.cobrancas_particular;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface CobrancaParticularApi {

    void criar(CriarCobrancaParticularCommand command);

    void atualizar(AtualizarCobrancaParticularCommand command);

    void cancelarPorAtendimento(Long atendimentoId);

    boolean possuiPendenciaPorAlunoId(UUID alunoId);

    Optional<CobrancaParticularSummary> buscarSummaryPorAtendimentoId(Long atendimentoId);

    Map<Long, CobrancaParticularSummary> buscarSummariesPorAtendimentoIds(
        Set<Long> atendimentoIds
    );
}
