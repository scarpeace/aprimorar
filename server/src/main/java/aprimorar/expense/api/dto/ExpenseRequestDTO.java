package aprimorar.expense.api.dto;

import aprimorar.expense.api.ExpenseCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequestDTO(
    @NotNull @Positive BigDecimal amount,
    @NotNull LocalDate date,
    @NotNull ExpenseCategory category,
    @NotBlank String description
) {}
