package aprimorar.financeiro.repasses.web.dto;

import aprimorar.financeiro.common.FormaPagamentoEnum;
import aprimorar.financeiro.repasses.repository.projections.RepasseLoteProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Lote de repasses pagos")
public record RepasseLoteResponse(
    @NotNull
    @Schema(nullable = false)
    UUID loteId,

    @NotNull
    @Schema(nullable = false)
    UUID colaboradorId,

    @NotNull
    @Schema(nullable = false)
    LocalDateTime dataRepasse,

    @NotNull
    @Schema(nullable = false)
    FormaPagamentoEnum formaPagamento,

    @Nullable
    @Schema(nullable = true)
    String comprovanteUrl,

    @NotNull
    @Schema(nullable = false)
    BigDecimal valorTotal,

    @NotNull
    @Schema(nullable = false)
    Long quantidadeRepasses
) {
    public static RepasseLoteResponse toDto(RepasseLoteProjection projection) {
        return new RepasseLoteResponse(
            projection.getLoteId(),
            projection.getColaboradorId(),
            projection.getDataRepasse(),
            projection.getFormaPagamento(),
            projection.getComprovanteUrl(),
            projection.getValorTotal(),
            projection.getQuantidadeRepasses()
        );
    }
}
