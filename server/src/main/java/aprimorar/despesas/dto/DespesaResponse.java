package aprimorar.despesas.dto;

import aprimorar.despesas.domain.Despesa;
import aprimorar.despesas.enums.CategoriaDespesa;
import aprimorar.despesas.enums.FormaPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Dados da despesa retornados pela API")
public record DespesaResponse(
    @Schema(description = "ID da despesa", example = "1")
    Long id,
    @Schema(description = "Título da despesa", example = "Conta de energia")
    String titulo,
    @Schema(description = "Categoria da despesa", example = "CONTAS")
    CategoriaDespesa categoria,
    @Schema(description = "Valor da despesa", example = "250.00")
    BigDecimal valor,
    @Schema(description = "Data de pagamento", example = "2026-07-22")
    LocalDate dataPagamento,
    @Schema(description = "Forma de pagamento", example = "PIX")
    FormaPagamento formaPagamento,
    @Schema(nullable = true, description = "Descrição curta da despesa")
    String descricao,
    @Schema(description = "Data de criação", example = "2026-07-22T10:00:00")
    LocalDateTime createdAt,
    @Schema(nullable = true, description = "Data de atualização", example = "2026-07-22T10:00:00")
    LocalDateTime updatedAt
) {
    public static DespesaResponse toDto(Despesa despesa) {
        return new DespesaResponse(
            despesa.getId(),
            despesa.getTitulo(),
            despesa.getCategoria(),
            despesa.getValor(),
            despesa.getDataPagamento(),
            despesa.getFormaPagamento(),
            despesa.getDescricao(),
            despesa.getCreatedAt(),
            despesa.getUpdatedAt()
        );
    }
}
