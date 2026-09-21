package aprimorar.financeiro.pagamentos_colaboradores.web.dto;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.pagamentos_colaboradores.domain.Pagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Pagamento de repasses particulares")
public record PagamentoResponse(
    @NotNull
    @Schema(nullable = false)
    UUID id,

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
    LocalDateTime createdAt
) {
    public static PagamentoResponse toDto(Pagamento pagamento) {
        return new PagamentoResponse(
            pagamento.getId(),
            pagamento.getDataPagamento(),
            pagamento.getValorTotal(),
            pagamento.getFormaPagamento(),
            pagamento.getComprovanteUrl(),
            pagamento.getCreatedAt()
        );
    }
}
