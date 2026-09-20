package aprimorar.financeiro.pagamentos_particular.web.dto;

import aprimorar.financeiro.common.FormaPagamentoEnum;
import aprimorar.financeiro.pagamentos_particular.domain.PagamentoParticular;
import aprimorar.financeiro.repasses_particular.domain.RepasseParticular;
import aprimorar.financeiro.repasses_particular.web.dto.RepasseParticularResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Detalhes de um pagamento de repasses particulares")
public record PagamentoParticularDetalheResponse(
    @NotNull
    @Schema(nullable = false)
    UUID id,

    @NotNull
    @Schema(nullable = false)
    UUID colaboradorId,

    @NotNull
    @Schema(nullable = false)
    LocalDate dataPagamento,

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
    List<RepasseParticularResponse> repasses
) {
    public static PagamentoParticularDetalheResponse from(
        PagamentoParticular pagamento,
        UUID colaboradorId,
        List<RepasseParticular> repasses
    ) {
        List<RepasseParticularResponse> repassesResponse = repasses.stream()
            .map(RepasseParticularResponse::from)
            .toList();

        return new PagamentoParticularDetalheResponse(
            pagamento.getId(),
            colaboradorId,
            pagamento.getDataPagamento(),
            pagamento.getValorTotal(),
            pagamento.getFormaPagamento(),
            pagamento.getComprovanteUrl(),
            pagamento.getCreatedAt(),
            repassesResponse
        );
    }
}
