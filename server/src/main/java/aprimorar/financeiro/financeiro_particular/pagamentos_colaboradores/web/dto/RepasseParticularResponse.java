package aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.web.dto;

import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.domain.RepasseParticular;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.domain.enums.StatusRepasseParticular;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Repasse particular")
public record RepasseParticularResponse(
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
    StatusRepasseParticular status,

    @NotNull
    @Schema(nullable = false)
    LocalDateTime createdAt
) {
    public static RepasseParticularResponse toDto(RepasseParticular repasse) {
        return new RepasseParticularResponse(
            repasse.getId(),
            repasse.getAtendimentoId(),
            repasse.getValor(),
            repasse.statusAtual(),
            repasse.getCreatedAt()
        );
    }
}
