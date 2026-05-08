package com.aprimorar.api.domain.finance.api.dto;

import com.aprimorar.api.enums.TransactionCategory;
import com.aprimorar.api.enums.TransactionOrigin;
import com.aprimorar.api.enums.TransactionStatus;
import com.aprimorar.api.enums.TransactionType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponseDTO(
    UUID id,
    TransactionType type,
    TransactionStatus status,
    BigDecimal amount,
    TransactionOrigin origin,
    UUID originId,
    Instant settledAt,
    LocalDate date,
    TransactionCategory category
) {
}
