package aprimorar.financeiro.cobrancas.web.dto;

import aprimorar.financeiro.common.FormaPagamentoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Detalhes de um lote de cobranças pagas")
public record CobrancaLoteDetalheResponse(
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
    Long quantidadeCobrancas,

    @NotNull
    @Schema(nullable = false)
    List<CobrancaLoteItemResponse> cobrancas
) {
    public static CobrancaLoteDetalheResponse from(
        CobrancaLoteResponse lote,
        List<CobrancaLoteItemResponse> cobrancas
    ) {
        return new CobrancaLoteDetalheResponse(
            lote.loteId(),
            lote.alunoId(),
            lote.dataPagamento(),
            lote.formaPagamento(),
            lote.comprovanteUrl(),
            lote.valorTotal(),
            lote.quantidadeCobrancas(),
            cobrancas
        );
    }
}
