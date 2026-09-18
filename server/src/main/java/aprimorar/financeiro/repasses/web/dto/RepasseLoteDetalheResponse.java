package aprimorar.financeiro.repasses.web.dto;

import aprimorar.financeiro.common.FormaPagamentoEnum;
import aprimorar.financeiro.repasses.domain.RepasseIndividual;
import aprimorar.financeiro.repasses.domain.enums.StatusRepasseIndividual;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Detalhes de um lote de repasses pagos")
public record RepasseLoteDetalheResponse(
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
    Long quantidadeRepasses,

    @NotNull
    @Schema(nullable = false)
    List<RepasseLoteItem> repasses
) {
    public static RepasseLoteDetalheResponse from(
        RepasseLoteResponse lote,
        List<RepasseLoteItem> repasses
    ) {
        return new RepasseLoteDetalheResponse(
            lote.loteId(),
            lote.colaboradorId(),
            lote.dataRepasse(),
            lote.formaPagamento(),
            lote.comprovanteUrl(),
            lote.valorTotal(),
            lote.quantidadeRepasses(),
            repasses
        );
    }

    @Schema(description = "Repasse individual pertencente a um lote de pagamento")
    public record RepasseLoteItem(
        @NotNull
        @Schema(nullable = false)
        Long id,

        @NotNull
        @Schema(nullable = false)
        Long atendimentoId,

        @NotNull
        @Schema(nullable = false)
        BigDecimal valor,

        @NotNull
        @Schema(nullable = false)
        StatusRepasseIndividual status
    ) {
        public static RepasseLoteItem from(RepasseIndividual repasse) {
            return new RepasseLoteItem(
                repasse.getId(),
                repasse.getAtendimentoId(),
                repasse.getValor(),
                repasse.getStatus()
            );
        }
    }
}
