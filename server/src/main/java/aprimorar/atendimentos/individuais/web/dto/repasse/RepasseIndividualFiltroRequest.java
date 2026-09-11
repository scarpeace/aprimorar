package aprimorar.atendimentos.individuais.web.dto.repasse;

import aprimorar.atendimentos.individuais.domain.enums.FormaPagamento;
import aprimorar.atendimentos.individuais.domain.enums.StatusRepasseIndividual;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Filtros opcionais para listar repasses individuais")
public record RepasseIndividualFiltroRequest(
    @Schema(description = "ID do colaborador")
    UUID colaboradorId,

    @Schema(description = "Status do repasse")
    StatusRepasseIndividual status,

    @Schema(description = "Forma de pagamento")
    FormaPagamento formaPagamento,

    @Schema(description = "Data inicial do repasse")
    LocalDate dataRepasseInicio,

    @Schema(description = "Data final do repasse")
    LocalDate dataRepasseFim
) {
    @AssertTrue(message = "A data inicial não pode ser posterior à data final")
    @Schema(hidden = true)
    public boolean periodoValido() {
        return dataRepasseInicio == null
            || dataRepasseFim == null
            || !dataRepasseInicio.isAfter(dataRepasseFim);
    }
}
