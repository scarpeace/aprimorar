package aprimorar.financeiro.repasses_colaboradores.api;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import aprimorar.financeiro.repasses_colaboradores.api.commands.AtualizarRepasseCommandApi;
import aprimorar.financeiro.repasses_colaboradores.api.commands.CriarRepasseCommandApi;
import aprimorar.financeiro.repasses_colaboradores.api.queries.RepasseSummary;

public interface PagamentosApi {

    void criarRepasse(CriarRepasseCommandApi command);

    void atualizarRepasse(AtualizarRepasseCommandApi command);

    void cancelarRepasse(Long atendimentoId);

    boolean possuiRepassePendente(UUID colaboradorId);

    RepasseSummary getRepasseSummaryPorAtendimento(Long atendimentoId);

    Map<Long, RepasseSummary> getRepassesSummariesPorAtendimentos(Set<Long> atendimentoIds);
}
