package aprimorar.financeiro.pagamentos_colaboradores.api.queries;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import aprimorar.financeiro.pagamentos_colaboradores.domain.Repasse;

public record RepasseQueryApi(
    Long id,
    Long atendimentoId,
    BigDecimal valor,
    String status,
    LocalDateTime createdAt,
    UUID pagamentoId
) {

    public static RepasseQueryApi toSummary(Repasse repasse) {
        return new RepasseQueryApi(
            repasse.getId(),
            repasse.getAtendimentoId(),
            repasse.getValor(),
            repasse.statusAtual().name(),
            repasse.getCreatedAt(),
            repasse.getPagamento() == null ? null : repasse.getPagamento().getId()
        );
    }
}
