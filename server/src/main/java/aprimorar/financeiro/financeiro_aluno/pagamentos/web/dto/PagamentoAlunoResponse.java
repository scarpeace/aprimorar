package aprimorar.financeiro.financeiro_aluno.pagamentos.web.dto;

import aprimorar.financeiro.common.FormaPagamentoEnum;
import aprimorar.financeiro.financeiro_aluno.pagamentos.repository.projections.PagamentoAlunoProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Pagamento de cobranças de um aluno")
public record PagamentoAlunoResponse(
    @NotNull
    @Schema(nullable = false)
    UUID id,

    @NotNull
    @Schema(nullable = false)
    UUID alunoId,

    @NotNull
    @Schema(nullable = false)
    LocalDate dataPagamento,

    @NotNull
    @Schema(nullable = false)
    FormaPagamentoEnum formaPagamento,

    @Nullable
    @Schema(nullable = true)
    String comprovanteUrl,

    @NotNull
    @Schema(nullable = false)
    BigDecimal valorTotal,

    @NotNull
    @Schema(nullable = false)
    Long quantidadeCobrancas
) {
    public static PagamentoAlunoResponse from(PagamentoAlunoProjection projection) {
        return new PagamentoAlunoResponse(
            projection.getId(),
            projection.getAlunoId(),
            projection.getDataPagamento(),
            projection.getFormaPagamento(),
            projection.getComprovanteUrl(),
            projection.getValorTotal(),
            projection.getQuantidadeCobrancas()
        );
    }
}
