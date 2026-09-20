package aprimorar.financeiro.cobrancas_particular.web.dto;

import aprimorar.financeiro.cobrancas_particular.domain.CobrancaParticular;
import aprimorar.financeiro.cobrancas_particular.domain.enums.StatusCobrancaParticular;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Cobrança particular")
public record CobrancaParticularResponse(
    @NotNull
    @Schema(nullable = false)
    Long id,

    @NotNull
    @Schema(nullable = false)
    Long atendimentoId,

    @NotNull
    @Schema(nullable = false)
    UUID alunoId,

    @NotNull
    @Schema(nullable = false)
    BigDecimal valor,

    @NotNull
    @Schema(nullable = false)
    StatusCobrancaParticular status,

    @NotNull
    @Schema(nullable = false)
    LocalDateTime createdAt,

    @Nullable
    @Schema(nullable = true)
    UUID recebimentoId
) {
    public static CobrancaParticularResponse toDto(CobrancaParticular cobranca) {
        return new CobrancaParticularResponse(
            cobranca.getId(),
            cobranca.getAtendimentoId(),
            cobranca.getAlunoId(),
            cobranca.getValor(),
            cobranca.statusAtual(),
            cobranca.getCreatedAt(),
            cobranca.getRecebimento() == null ? null : cobranca.getRecebimento().getId()
        );
    }
}
