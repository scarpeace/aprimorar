package aprimorar.financeiro.pagamentos_colaboradores.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import aprimorar.financeiro.pagamentos_colaboradores.domain.Repasse;
import aprimorar.financeiro.pagamentos_colaboradores.domain.enums.StatusRepasse;

@Schema(description = "Repasse particular")
public record RepasseResponse(
    @NotNull
    @Schema(nullable = false)
    Long id,

    @NotNull
    @Schema(nullable = false)
    Long atendimentoId,

    @NotNull
    @Schema(nullable = false)
    UUID colaboradorId,

    @NotNull
    @Schema(nullable = false)
    BigDecimal valor,

    @NotNull
    @Schema(nullable = false)
    StatusRepasse status,

    @NotNull
    @Schema(nullable = false)
    LocalDateTime createdAt,

    @Nullable
    @Schema(nullable = true)
    UUID pagamentoId
) {
    public static RepasseResponse toDto(Repasse repasse) {
        return new RepasseResponse(
            repasse.getId(),
            repasse.getAtendimentoId(),
            repasse.getColaboradorId(),
            repasse.getValor(),
            repasse.statusAtual(),
            repasse.getCreatedAt(),
            repasse.getPagamento() == null ? null : repasse.getPagamento().getId()
        );
    }
}
