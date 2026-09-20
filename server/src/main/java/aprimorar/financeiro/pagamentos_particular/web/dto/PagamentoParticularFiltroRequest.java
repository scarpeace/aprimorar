package aprimorar.financeiro.pagamentos_particular.web.dto;

import aprimorar.financeiro.common.FormaPagamentoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Filtros opcionais para listar pagamentos de repasses particulares")
public record PagamentoParticularFiltroRequest(
    @Schema(description = "ID do colaborador")
    UUID colaboradorId,

    @Schema(description = "Forma de pagamento")
    FormaPagamentoEnum formaPagamento,

    @Schema(description = "Data inicial do pagamento", format = "date")
    LocalDate dataPagamentoInicio,

    @Schema(description = "Data final do pagamento", format = "date")
    LocalDate dataPagamentoFim
) {
}
