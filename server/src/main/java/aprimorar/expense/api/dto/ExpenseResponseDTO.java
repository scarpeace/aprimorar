package aprimorar.expense.api.dto;

import aprimorar.expense.api.ExpenseCategory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseResponseDTO(
    UUID id,
    BigDecimal amount,
    LocalDate date,
    ExpenseCategory category,
    String description
) {}
