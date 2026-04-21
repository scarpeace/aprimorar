package com.aprimorar.api.domain.finance.dto;

import com.aprimorar.api.enums.GeneralExpenseCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GeneralExpenseRequestDTO(
        @NotBlank String description,
        @NotNull @Positive BigDecimal amount,
        @NotNull LocalDate date,
        @NotNull GeneralExpenseCategory category
) {}
