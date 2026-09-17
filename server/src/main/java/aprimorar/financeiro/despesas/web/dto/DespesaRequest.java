package aprimorar.financeiro.despesas.web.dto;

import aprimorar.financeiro.despesas.domain.enums.CategoriaDespesa;
import aprimorar.financeiro.despesas.domain.enums.FormaPagamento;
import aprimorar.financeiro.despesas.domain.enums.TipoDespesa;
import aprimorar.financeiro.despesas.domain.DespesaEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Payload para criar ou atualizar uma despesa")
public record DespesaRequest(
    @NotBlank(message = "Título da despesa é obrigatório")
    @Size(max = 120, message = "Título da despesa deve ter no máximo 120 caracteres")
    @Schema(nullable = false, description = "Título da despesa", example = "Conta de energia")
    String titulo,

    @NotNull(message = "Tipo da despesa é obrigatório")
    @Schema(nullable = false, description = "Tipo do lançamento financeiro", example = "SAIDA")
    TipoDespesa tipo,

    @NotNull(message = "Categoria da despesa é obrigatória")
    @Schema(nullable = false, description = "Categoria da despesa", example = "CONTAS")
    CategoriaDespesa categoria,

    @NotNull(message = "Valor da despesa é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor da despesa deve ser maior que zero")
    @Schema(nullable = false, description = "Valor da despesa", example = "250.00")
    BigDecimal valor,

    @NotNull(message = "Data de vencimento é obrigatória")
    @Schema(nullable = false, description = "Data de vencimento", example = "2026-07-22")
    LocalDate dataVencimento,

    @NotNull(message = "Forma de pagamento é obrigatória")
    @Schema(nullable = false, description = "Forma de pagamento", example = "PIX")
    FormaPagamento formaPagamento,

    @Size(max = 500, message = "Descrição da despesa deve ter no máximo 500 caracteres")
    @Schema(nullable = true, description = "Descrição curta da despesa", example = "Pagamento referente ao mês de julho")
    String descricao
) {
    public DespesaEntity toEntity() {
        return new DespesaEntity(
            titulo,
            tipo,
            categoria,
            valor,
            dataVencimento,
            formaPagamento,
            normalizeDescription(descricao)
        );
    }

    private static String normalizeDescription(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
