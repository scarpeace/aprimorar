package aprimorar.financeiro.repasses_colaboradores.web.dto;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.repasses_colaboradores.domain.RepassePagamento;
import aprimorar.financeiro.repasses_colaboradores.domain.Repasse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Detalhes de um pagamento de repasses")
public record PagamentoDetalheResponse(
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
    List<RepasseResponse> repasses
) {
    public static PagamentoDetalheResponse toDto(RepassePagamento pagamento) {
        List<Repasse> repasses = pagamento.getRepasses();

        List<RepasseResponse> repassesDto = repasses.stream()
            .map(RepasseResponse::toDto)
            .toList();

        return new PagamentoDetalheResponse(
            pagamento.getId(),
            repasses.getFirst().getColaboradorId(),
            pagamento.getDataPagamento(),
            pagamento.getValorTotal(),
            pagamento.getFormaPagamento(),
            pagamento.getComprovanteUrl(),
            pagamento.getCreatedAt(),
            repassesDto
        );
    }
}
