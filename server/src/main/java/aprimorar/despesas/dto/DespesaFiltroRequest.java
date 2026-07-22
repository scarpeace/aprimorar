package aprimorar.despesas.dto;

import aprimorar.despesas.enums.CategoriaDespesa;
import aprimorar.despesas.enums.FormaPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import java.time.LocalDate;

@Schema(description = "Filtros opcionais para listar despesas")
public record DespesaFiltroRequest(
    @Schema(description = "Texto livre de busca", nullable = true)
    String busca,
    @Schema(description = "Categoria da despesa", nullable = true)
    CategoriaDespesa categoria,
    @Schema(description = "Forma de pagamento", nullable = true)
    FormaPagamento formaPagamento,
    @Schema(description = "Data inicial de pagamento", format = "date", nullable = true)
    LocalDate dataInicio,
    @Schema(description = "Data final de pagamento", format = "date", nullable = true)
    LocalDate dataFim
) {
    @AssertTrue(message = "Data inicial não pode ser posterior à data final")
    @Schema(hidden = true)
    public boolean getPeriodoValido() {
        return dataInicio == null || dataFim == null || !dataInicio.isAfter(dataFim);
    }
}
