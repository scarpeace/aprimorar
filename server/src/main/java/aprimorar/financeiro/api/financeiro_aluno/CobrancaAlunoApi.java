package aprimorar.financeiro.api.financeiro_aluno;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface CobrancaAlunoApi {

    void criar(CriarCobrancaAlunoCommand command);

    void atualizar(AtualizarCobrancaAlunoCommand command);

    void cancelarPorOrigem(Long origemId, TipoOrigemCobrancaAluno origemTipo);

    boolean possuiPendenciaPorAlunoId(UUID alunoId);

    Optional<CobrancaAlunoResumo> buscarResumoPorOrigemId(
        Long origemId,
        TipoOrigemCobrancaAluno origemTipo
    );

    Map<Long, CobrancaAlunoResumo> buscarResumosPorOrigemIds(
        Set<Long> origemIds,
        TipoOrigemCobrancaAluno origemTipo
    );
}
