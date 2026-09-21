package aprimorar.financeiro.financeiro_particular.recebimentos_alunos.api;

import java.math.BigDecimal;
import java.util.UUID;

public record AtualizarCobrancaParticularCommand(
    Long atendimentoId,
    UUID alunoId,
    BigDecimal valor
) {
}
