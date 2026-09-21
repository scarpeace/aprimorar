package aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.web.dto;

import aprimorar.common.FormaPagamentoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Filtros opcionais para listar pagamentos de repasses particulares")
public record PagamentoParticularFiltroRequest(
    @Schema(description = "ID do colaborador")
    UUID colaboradorId,

    @Schema(description = "Forma de pagamento")
    FormaPagamentoEnum formaPagamento,

    @PastOrPresent(message = "A data inicial não pode ser futura")
    @Schema(description = "Data inicial do pagamento", format = "date")
    LocalDate dataPagamentoInicio,

    @PastOrPresent(message = "A data final não pode ser futura")
    @Schema(description = "Data final do pagamento", format = "date")
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
