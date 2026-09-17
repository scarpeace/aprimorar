package aprimorar.financeiro.cobrancas.web.dto;


import aprimorar.financeiro.cobrancas.domain.CobrancaIndividual;
import aprimorar.financeiro.cobrancas.domain.enums.StatusCobrancaIndividual;
import aprimorar.financeiro.common.FormaPagamentoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Cobrança de aluno")
public record CobrancaIndividualResponse(
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
    StatusCobrancaIndividual status,

    @Nullable
    @Schema(nullable = true)
    LocalDateTime dataPagamento,

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
    public static CobrancaIndividualResponse toDto(CobrancaIndividual entity) {
        return new CobrancaIndividualResponse(
            entity.getId(),
            entity.getAtendimentoId(),
            entity.getAlunoId(),
            entity.getValor(),
            entity.getStatus(),
            entity.getDataPagamento(),
            entity.getFormaPagamento(),
            entity.getComprovanteUrl(),
            entity.getLoteId()
        );
    }

}
