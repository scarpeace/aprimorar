package aprimorar.financeiro.web.dto;

import aprimorar.financeiro.domain.CategoriaDespesaEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;

@Schema(description = "Payload para criação ou atualização de despesa")
public record DespesaRequestDTO(
    @NotNull
    @Positive
    @Schema(description = "Valor da despesa", example = "150.00")
    BigDecimal amount,

    @NotNull
    @Schema(description = "Data de competência da despesa", example = "2026-05-24T00:00:00Z")
    Instant date,

    @NotNull
    @Schema(description = "Categoria da despesa", example = "CONTAS")
    CategoriaDespesaEnum category,

    @NotBlank
    @Schema(description = "Descrição da despesa", example = "Pagamento de internet")
    String description
) {}
