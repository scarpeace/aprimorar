package aprimorar.financeiro.api.dto;

import aprimorar.financeiro.api.CategoriaDespesa;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record DespesaResponseDTO(
    UUID id,
    BigDecimal amount,
    LocalDate date,
    CategoriaDespesa category,
    String description,
    @Nullable
    @Schema(nullable = true, description = "Data de pagamento da despesa", example = "2024-03-10T15:33:42Z")
    Instant paymentDate
) {}
