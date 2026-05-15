package aprimorar.expense.api.dto;

import aprimorar.expense.api.ExpenseCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseResponseDTO(
    UUID id,
    BigDecimal amount,
    LocalDate date,
    ExpenseCategory category,
    String description,
    @Nullable
    @Schema(nullable = true, description = "Data de pagamento da despesa", example = "2024-03-10T15:33:42Z")
    Instant paymentDate
) {}
