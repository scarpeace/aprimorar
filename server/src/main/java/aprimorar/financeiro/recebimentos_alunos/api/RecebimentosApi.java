package aprimorar.financeiro.recebimentos_alunos.api;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import aprimorar.financeiro.recebimentos_alunos.api.commands.AtualizarCobrancaCommandApi;
import aprimorar.financeiro.recebimentos_alunos.api.commands.CriarCobrancaCommandApi;
import aprimorar.financeiro.recebimentos_alunos.api.queries.CobrancaSummary;

public interface RecebimentosApi {

    void criarCobranca(CriarCobrancaCommandApi command);

    void atualizarCobranca(AtualizarCobrancaCommandApi command);

    void cancelarCobranca(Long atendimentoId);

    boolean possuiCobrancaPendente(UUID alunoId);

    CobrancaSummary getCobrancaQueryApiPorAtendimento(Long atendimentoId);

    Map<Long, CobrancaSummary> getCobrancasQueryApisPorAtendimentos(
        Set<Long> atendimentoIds
    );
}
