package aprimorar.financeiro.financeiro_particular.recebimentos_alunos.web.dto;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.domain.RecebimentoParticular;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Recebimento particular")
public record RecebimentoParticularResponse(
    @NotNull
    @Schema(nullable = false)
    UUID id,

    @NotNull
    @Schema(nullable = false)
    LocalDate dataRecebimento,

    @NotNull
    @Schema(nullable = false)
    BigDecimal valorTotal,

    @NotNull
    @Schema(nullable = false)
    FormaPagamentoEnum formaPagamento,

    @Nullable
    @Schema(nullable = true)
    String comprovanteUrl,

    @NotNull
    @Schema(nullable = false)
    LocalDateTime createdAt
) {
    public static RecebimentoParticularResponse toDto(RecebimentoParticular recebimento) {
        return new RecebimentoParticularResponse(
            recebimento.getId(),
            recebimento.getDataRecebimento(),
            recebimento.getValorTotal(),
            recebimento.getFormaPagamento(),
            recebimento.getComprovanteUrl(),
            recebimento.getCreatedAt()
        );
    }
}
