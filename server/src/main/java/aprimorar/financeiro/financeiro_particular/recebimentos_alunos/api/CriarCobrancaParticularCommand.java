package aprimorar.financeiro.financeiro_particular.recebimentos_alunos.api;

import java.math.BigDecimal;
import java.util.UUID;

public record CriarCobrancaParticularCommand(
    Long atendimentoId,
    UUID alunoId,
    BigDecimal valor
) {
}
