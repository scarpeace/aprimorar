package aprimorar.financeiro.api.dto;

import aprimorar.financeiro.api.CategoriaDespesaEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Dados de despesa retornados pela API")
public record DespesaResponseDTO(
    @Schema(description = "Identificador unico da despesa", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,
    @Schema(description = "Valor da despesa", example = "150.00")
    BigDecimal amount,
    @Schema(description = "Data de competencia da despesa", example = "2026-05-24T00:00:00Z")
    Instant date,
    @Schema(description = "Categoria da despesa", example = "OPERACIONAL")
    CategoriaDespesaEnum category,
    @Schema(description = "Descricao da despesa", example = "Pagamento de internet")
    String description,
    @Nullable
    @Schema(nullable = true, description = "Data de pagamento da despesa", example = "2024-03-10T15:33:42Z")
    Instant paymentDate,
    @Schema(description = "Data de criação da despesa", example = "2024-03-10T15:33:42Z")
    Instant createdAt,
    @Schema(description = "Data de atualização da despesa", example = "2024-03-10T15:33:42Z")
    Instant updatedAt
) {}
