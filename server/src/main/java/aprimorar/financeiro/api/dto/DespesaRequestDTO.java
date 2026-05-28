package aprimorar.financeiro.api.dto;

import aprimorar.financeiro.api.CategoriaDespesaEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;

@Schema(description = "Payload para criacao ou atualizacao de despesa")
public record DespesaRequestDTO(
    @NotNull
    @Positive
    @Schema(description = "Valor da despesa", example = "150.00")
    BigDecimal amount,

    @NotNull
    @Schema(description = "Data de competencia da despesa", example = "2026-05-24T00:00:00Z")
    Instant date,

    @NotNull
    @Schema(description = "Categoria da despesa", example = "OPERACIONAL")
    CategoriaDespesaEnum category,

    @NotBlank
    @Schema(description = "Descricao da despesa", example = "Pagamento de internet")
    String description
) {}
