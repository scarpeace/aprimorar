package aprimorar.financeiro.recebimentos_alunos.api.queries;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import aprimorar.financeiro.recebimentos_alunos.domain.Cobranca;

public record CobrancaSummary(
    Long id,
    Long atendimentoId,
    BigDecimal valor,
    String status,
    LocalDateTime createdAt,
    UUID recebimentoId
) {

    public static CobrancaSummary toSummary(Cobranca cobranca) {
        return new CobrancaSummary(
            cobranca.getId(),
            cobranca.getAtendimentoId(),
            cobranca.getValor(),
            cobranca.statusAtual().name(),
            cobranca.getCreatedAt(),
            cobranca.getRecebimento() == null ? null : cobranca.getRecebimento().getId()
        );
    }
}
