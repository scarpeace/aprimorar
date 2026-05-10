package aprimorar.finance.api.dto;

import aprimorar.shared.enums.TransactionCategory;
import aprimorar.shared.enums.TransactionOrigin;
import aprimorar.shared.enums.TransactionStatus;
import aprimorar.shared.enums.TransactionType;
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
