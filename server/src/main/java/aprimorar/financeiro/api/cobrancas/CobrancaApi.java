package aprimorar.financeiro.api.cobrancas;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface CobrancaApi {

    void criar(CriarCobrancaCommand command);

    void atualizar(AtualizarCobrancaCommand command);

    void cancelarPorAtendimento(Long atendimentoId);

    boolean possuiPendenciaPorAlunoId(UUID alunoId);

    Map<Long, CobrancaResumo> buscarResumosPorAtendimentoIds(Set<Long> atendimentoIds);
}
