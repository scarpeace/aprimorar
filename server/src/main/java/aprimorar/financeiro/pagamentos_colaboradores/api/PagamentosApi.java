package aprimorar.financeiro.pagamentos_colaboradores.api;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import aprimorar.financeiro.pagamentos_colaboradores.api.commands.AtualizarRepasseCommandApi;
import aprimorar.financeiro.pagamentos_colaboradores.api.commands.CriarRepasseCommandApi;
import aprimorar.financeiro.pagamentos_colaboradores.api.queries.RepasseQueryApi;

public interface PagamentosApi {

    void criarRepasse(CriarRepasseCommandApi command);

    void atualizarRepasse(AtualizarRepasseCommandApi command);

    void cancelarRepasse(Long atendimentoId);

    boolean possuiRepassePendente(UUID colaboradorId);

    RepasseQueryApi getRepasseQueryApiPorAtentimento(Long atendimentoId);

    Map<Long, RepasseQueryApi> getRepassesQueryApisPorAtendimentos(Set<Long> atendimentoIds);
}
