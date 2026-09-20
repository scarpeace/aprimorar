package aprimorar.financeiro.api.cobrancas_particular;

import java.math.BigDecimal;
import java.util.UUID;

public record CriarCobrancaParticularCommand(
    Long atendimentoId,
    UUID alunoId,
    BigDecimal valor
) {
}
