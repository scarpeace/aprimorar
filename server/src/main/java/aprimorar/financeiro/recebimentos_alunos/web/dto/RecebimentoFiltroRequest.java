package aprimorar.financeiro.recebimentos_alunos.web.dto;

import aprimorar.common.FormaPagamentoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Filtros opcionais para listar recebimentos")
public record RecebimentoFiltroRequest(
    @Schema(description = "ID do aluno")
    UUID alunoId,

    @Schema(description = "Forma de pagamento")
    FormaPagamentoEnum formaPagamento,

    @PastOrPresent(message = "A data inicial não pode ser futura")
    @Schema(description = "Data inicial do recebimento")
    LocalDate dataRecebimentoInicio,

    @PastOrPresent(message = "A data final não pode ser futura")
    @Schema(description = "Data final do recebimento")
    LocalDate dataRecebimentoFim
) {
    @AssertTrue(message = "A data inicial não pode ser posterior à data final")
    @Schema(hidden = true)
    public boolean periodoValido() {
        return dataRecebimentoInicio == null
            || dataRecebimentoFim == null
            || !dataRecebimentoInicio.isAfter(dataRecebimentoFim);
    }
}
