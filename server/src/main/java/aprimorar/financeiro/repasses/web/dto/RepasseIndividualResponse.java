package aprimorar.financeiro.repasses.web.dto;


import aprimorar.financeiro.repasses.domain.enums.StatusRepasseIndividual;
import aprimorar.financeiro.common.FormaPagamentoEnum;
import aprimorar.financeiro.repasses.domain.RepasseIndividual;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Repasse individual")
public record RepasseIndividualResponse(
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
    StatusRepasseIndividual status,

    @Nullable
    @Schema(nullable = true)
    LocalDateTime dataRepasse,

    @Nullable
    @Schema(nullable = true)
    FormaPagamentoEnum formaPagamento,

    @Nullable
    @Schema(nullable = true)
    String comprovanteUrl,

    @Nullable
    @Schema(nullable = true)
    UUID loteId
) {
    public static RepasseIndividualResponse toDto(RepasseIndividual entity) {
        return new RepasseIndividualResponse(
            entity.getId(), entity.getAtendimentoId(), entity.getColaboradorId(), entity.getValor(),
            entity.getStatus(), entity.getDataRepasse(), entity.getFormaPagamento(), entity.getComprovanteUrl(),
            entity.getLoteId()
        );
    }

}
