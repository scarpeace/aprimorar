package aprimorar.atendimentos.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import aprimorar.atendimentos.enums.CategoriaTransacao;
import aprimorar.atendimentos.enums.FormaPagamento;
import aprimorar.atendimentos.enums.StatusTransacao;
import aprimorar.atendimentos.enums.TipoTransacao;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Filtros opcionais para listar transações")
public record TransacaoFiltroRequest(
    @Schema(description = "ID do pagador", nullable = true)
    UUID pagadorId,
    @Schema(description = "ID do recebedor", nullable = true)
    UUID recebedorId,
    @Schema(description = "Data de efetivação", format = "date-time", nullable = true)
    LocalDateTime dataEfetivada,
    @Schema(description = "Tipo da transação", nullable = true)
    TipoTransacao tipo,
    @Schema(description = "Categoria financeira", nullable = true)
    CategoriaTransacao categoria,
    @Schema(description = "Status da transação", nullable = true)
    StatusTransacao status,
    @Schema(description = "Forma de pagamento", nullable = true)
    FormaPagamento formaPagamento,
    @Schema(description = "Busca por nome do pagador ou recebedor", nullable = true)
    String busca
) {
}
