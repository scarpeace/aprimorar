package aprimorar.financeiro.cobrancas.web.dto;

import aprimorar.financeiro.cobrancas.domain.CobrancaIndividual;
import aprimorar.financeiro.cobrancas.domain.enums.StatusCobrancaIndividual;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "Cobrança individual pertencente a um lote de pagamento")
public record CobrancaLoteItemResponse(
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
    StatusCobrancaIndividual status
) {
    public static CobrancaLoteItemResponse toDto(CobrancaIndividual cobranca) {
        return new CobrancaLoteItemResponse(
            cobranca.getId(),
            cobranca.getAtendimentoId(),
            cobranca.getValor(),
            cobranca.getStatus()
        );
    }
}
