package aprimorar.atendimentos.dto;

import java.math.BigDecimal;

import aprimorar.atendimentos.enums.CategoriaTransacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Payload para criar uma despesa manual")
public record TransacaoRequestDTO(
    @NotNull(message = "Informe o valor")
    @Positive(message = "O valor precisa ser maior que zero")
    @Schema(description = "Valor da despesa", example = "150.00")
    BigDecimal valor,

    @NotNull(message = "Escolha a categoria")
    @Schema(description = "Categoria da despesa", example = "DESPENSA", nullable = false)
    CategoriaTransacao categoria
) {
}
