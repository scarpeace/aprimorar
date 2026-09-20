package aprimorar.financeiro.recebimentos_particular.web.dto;

import aprimorar.financeiro.common.FormaPagamentoEnum;
import aprimorar.financeiro.cobrancas_particular.domain.CobrancaParticular;
import aprimorar.financeiro.cobrancas_particular.web.dto.CobrancaParticularResponse;
import aprimorar.financeiro.recebimentos_particular.domain.RecebimentoParticular;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Detalhes de um recebimento de cobranças particulares")
public record RecebimentoParticularDetalheResponse(
    @NotNull
    @Schema(nullable = false)
    UUID id,

    @NotNull
    @Schema(nullable = false)
    UUID alunoId,

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
    LocalDateTime createdAt,

    @NotNull
    @Schema(nullable = false)
    List<CobrancaParticularResponse> cobrancas
) {
    public static RecebimentoParticularDetalheResponse toDto(
        RecebimentoParticular recebimento
    ) {
        List<CobrancaParticular> cobrancas = recebimento.getCobrancas();

        return new RecebimentoParticularDetalheResponse(
            recebimento.getId(),
            cobrancas.getFirst().getAlunoId(),
            recebimento.getDataRecebimento(),
            recebimento.getValorTotal(),
            recebimento.getFormaPagamento(),
            recebimento.getComprovanteUrl(),
            recebimento.getCreatedAt(),
            cobrancas.stream().map(CobrancaParticularResponse::toDto).toList()
        );
    }
}
