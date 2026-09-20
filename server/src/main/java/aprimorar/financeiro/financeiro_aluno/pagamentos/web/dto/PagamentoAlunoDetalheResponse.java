package aprimorar.financeiro.financeiro_aluno.pagamentos.web.dto;

import aprimorar.financeiro.common.FormaPagamentoEnum;
import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.CobrancaAluno;
import aprimorar.financeiro.financeiro_aluno.pagamentos.domain.PagamentoAluno;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Schema(description = "Detalhes de um pagamento de cobranças de aluno")
public record PagamentoAlunoDetalheResponse(
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
    List<PagamentoAlunoCobrancaResponse> cobrancas
) {
    public static PagamentoAlunoDetalheResponse from(
        PagamentoAluno pagamento,
        List<CobrancaAluno> cobrancas
    ) {
        BigDecimal valorTotal = cobrancas.stream()
            .map(CobrancaAluno::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<PagamentoAlunoCobrancaResponse> cobrancasResponse = cobrancas.stream()
            .map(PagamentoAlunoCobrancaResponse::from)
            .toList();

        return new PagamentoAlunoDetalheResponse(
            pagamento.getId(),
            pagamento.getAlunoId(),
            pagamento.getDataPagamento(),
            pagamento.getFormaPagamento(),
            pagamento.getComprovanteUrl(),
            valorTotal,
            cobrancasResponse
        );
    }
}
