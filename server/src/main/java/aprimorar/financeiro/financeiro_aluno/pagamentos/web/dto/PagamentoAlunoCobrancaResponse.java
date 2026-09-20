package aprimorar.financeiro.financeiro_aluno.pagamentos.web.dto;

import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.CobrancaAluno;
import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.enums.StatusCobrancaAluno;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "Cobrança vinculada a um pagamento de aluno")
public record PagamentoAlunoCobrancaResponse(
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
    StatusCobrancaAluno status
) {
    public static PagamentoAlunoCobrancaResponse from(CobrancaAluno cobranca) {
        return new PagamentoAlunoCobrancaResponse(
            cobranca.getId(),
            cobranca.getAtendimentoId(),
            cobranca.getValor(),
            cobranca.getStatus()
        );
    }
}
