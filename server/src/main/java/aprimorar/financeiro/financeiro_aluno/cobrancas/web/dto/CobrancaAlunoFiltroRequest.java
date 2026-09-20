package aprimorar.financeiro.financeiro_aluno.cobrancas.web.dto;

import aprimorar.financeiro.common.FormaPagamentoEnum;
import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.enums.StatusCobrancaAluno;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Filtros opcionais para listar cobranças de alunos")
public record CobrancaAlunoFiltroRequest(
    @Schema(description = "ID do aluno")
    UUID alunoId,

    @Schema(description = "Status da cobrança")
    StatusCobrancaAluno status,

    @Schema(description = "Forma de pagamento")
    FormaPagamentoEnum formaPagamento,

    @Schema(description = "Data inicial do pagamento")
    LocalDate dataPagamentoInicio,

    @Schema(description = "Data final do pagamento")
    LocalDate dataPagamentoFim
) {
    @AssertTrue(message = "A data inicial não pode ser posterior à data final")
    @Schema(hidden = true)
    public boolean periodoValido() {
        return dataPagamentoInicio == null
            || dataPagamentoFim == null
            || !dataPagamentoInicio.isAfter(dataPagamentoFim);
    }
}
