package aprimorar.despesas.dto;

import aprimorar.despesas.domain.Despesa;
import aprimorar.despesas.enums.CategoriaDespesa;
import aprimorar.despesas.enums.FormaPagamento;
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

    @NotNull(message = "Categoria da despesa é obrigatória")
    @Schema(nullable = false, description = "Categoria da despesa", example = "CONTAS")
    CategoriaDespesa categoria,

    @NotNull(message = "Valor da despesa é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor da despesa deve ser maior que zero")
    @Schema(nullable = false, description = "Valor da despesa", example = "250.00")
    BigDecimal valor,

    @NotNull(message = "Data de pagamento é obrigatória")
    @Schema(nullable = false, description = "Data de pagamento", example = "2026-07-22")
    LocalDate dataPagamento,

    @NotNull(message = "Forma de pagamento é obrigatória")
    @Schema(nullable = false, description = "Forma de pagamento", example = "PIX")
    FormaPagamento formaPagamento,

    @Size(max = 500, message = "Descrição da despesa deve ter no máximo 500 caracteres")
    @Schema(nullable = true, description = "Descrição curta da despesa", example = "Pagamento referente ao mês de julho")
    String descricao
) {
    public Despesa toEntity() {
        return new Despesa(
            titulo.trim(),
            categoria,
            valor,
            dataPagamento,
            formaPagamento,
            normalizeDescription(descricao)
        );
    }

    private static String normalizeDescription(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
