package aprimorar.financeiro.api.repasses;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface RepasseApi {

    void criar(CriarRepasseCommand command);

    void atualizar(AtualizarRepasseCommand command);

    void cancelarPorAtendimento(Long atendimentoId);

    boolean possuiPendenciaPorColaboradorId(UUID colaboradorId);

    Map<Long, RepasseResumo> buscarResumosPorAtendimentoIds(Set<Long> atendimentoIds);
}
