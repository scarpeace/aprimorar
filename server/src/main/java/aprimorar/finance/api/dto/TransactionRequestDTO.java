package aprimorar.finance.api.dto;

import aprimorar.finance.api.enums.TransactionCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequestDTO(
    @NotNull @Positive BigDecimal amount,
    @NotNull LocalDate date,
    @NotNull TransactionCategory category
) {}
