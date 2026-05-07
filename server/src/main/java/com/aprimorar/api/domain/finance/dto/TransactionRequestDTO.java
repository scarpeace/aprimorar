package com.aprimorar.api.domain.finance.dto;

import com.aprimorar.api.enums.TransactionCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequestDTO(
    @NotNull @Positive BigDecimal amount,
    @NotNull LocalDate date,
    @NotNull TransactionCategory category
) {}
