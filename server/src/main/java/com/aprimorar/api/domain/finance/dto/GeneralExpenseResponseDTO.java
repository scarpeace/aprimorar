package com.aprimorar.api.domain.finance.dto;

import com.aprimorar.api.domain.finance.GeneralExpense;
import com.aprimorar.api.enums.GeneralExpenseCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record GeneralExpenseResponseDTO(
        UUID id,
        String description,
        BigDecimal amount,
        LocalDate date,
        GeneralExpenseCategory category
) {
    public static GeneralExpenseResponseDTO fromEntity(GeneralExpense expense) {
        return new GeneralExpenseResponseDTO(
                expense.getId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getDate(),
                expense.getCategory()
        );
    }
}
