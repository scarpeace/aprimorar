package aprimorar.financeiro.cobrancas.web.dto;

import aprimorar.financeiro.common.FormaPagamentoEnum;
import aprimorar.financeiro.cobrancas.repository.projections.CobrancaLoteProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Lote de cobranças pagas")
public record CobrancaLoteResponse(
    @NotNull
    @Schema(nullable = false)
    UUID loteId,

    @NotNull
    @Schema(nullable = false)
    UUID alunoId,

    @NotNull
    @Schema(nullable = false)
    LocalDateTime dataPagamento,

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
    Long quantidadeCobrancas
) {
    public static CobrancaLoteResponse toDto(CobrancaLoteProjection projection) {
        return new CobrancaLoteResponse(
            projection.getLoteId(),
            projection.getAlunoId(),
            projection.getDataPagamento(),
            projection.getFormaPagamento(),
            projection.getComprovanteUrl(),
            projection.getValorTotal(),
            projection.getQuantidadeCobrancas()
        );
    }
}
