package aprimorar.financeiro.repasses_colaboradores.api.queries;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import aprimorar.financeiro.repasses_colaboradores.domain.Repasse;

public record RepasseSummary(
    Long id,
    Long atendimentoId,
    BigDecimal valor,
    String status,
    LocalDateTime createdAt,
    UUID pagamentoId
) {

    public static RepasseSummary toSummary(Repasse repasse) {
        return new RepasseSummary(
            repasse.getId(),
            repasse.getAtendimentoId(),
            repasse.getValor(),
            repasse.getStatus().name(),
            repasse.getCreatedAt(),
            repasse.getPagamento() == null ? null : repasse.getPagamento().getId()
        );
    }
}
