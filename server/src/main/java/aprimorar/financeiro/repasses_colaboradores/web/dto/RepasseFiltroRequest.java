package aprimorar.financeiro.repasses_colaboradores.web.dto;

import aprimorar.common.FormaPagamentoEnum;
import aprimorar.financeiro.repasses_colaboradores.domain.enums.StatusRepasse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Filtros opcionais para listar repasses particulares")
public record RepasseFiltroRequest(
    @Schema(description = "ID do colaborador")
    UUID colaboradorId,

    @Schema(description = "Status do repasse")
    StatusRepasse status,

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
