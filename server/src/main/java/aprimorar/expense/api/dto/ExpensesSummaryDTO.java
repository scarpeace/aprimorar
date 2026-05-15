package aprimorar.expense.api.dto;

import aprimorar.shared.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Despesas paginadas e resumo financeiro do periodo consultado")
public record ExpensesSummaryDTO(
    @Schema(description = "Despesas gerais, paginadas e filtradas conforme os parametros informados")
    PageDTO<ExpenseResponseDTO> expenses,

    @Schema(description = "Total de despesas gerais no periodo")
    BigDecimal totalExpenses,

    @Schema(description = "Total de despesas gerais pendentes de pagamento no periodo")
    BigDecimal pendingExpenses
) {}
