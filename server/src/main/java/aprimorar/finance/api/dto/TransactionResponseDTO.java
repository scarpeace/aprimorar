package aprimorar.finance.api.dto;

import aprimorar.finance.api.enums.TransactionCategory;

import aprimorar.finance.api.enums.TransactionOrigin;

import aprimorar.finance.api.enums.TransactionStatus;

import aprimorar.finance.api.enums.TransactionType;
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
