package com.aprimorar.api.domain.finance.dto;

import com.aprimorar.api.domain.finance.Transaction;
import com.aprimorar.api.enums.TransactionCategory;
import com.aprimorar.api.enums.TransactionOrigin;
import com.aprimorar.api.enums.TransactionStatus;
import com.aprimorar.api.enums.TransactionType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
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
    public static TransactionResponseDTO fromEntity(Transaction transaction) {
        return new TransactionResponseDTO(
            transaction.getId(),
            transaction.getType(),
            transaction.getStatus(),
            transaction.getAmount(),
            transaction.getOrigin(),
            transaction.getOriginId(),
            transaction.getSettledAt(),
            transaction.getSettledAt() == null ? null : transaction.getSettledAt().atZone(ZoneOffset.UTC).toLocalDate(),
            transaction.getCategory()
        );
    }
}
