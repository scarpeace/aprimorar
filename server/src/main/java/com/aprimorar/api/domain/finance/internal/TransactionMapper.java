package com.aprimorar.api.domain.finance.internal;

import com.aprimorar.api.domain.finance.api.dto.TransactionResponseDTO;
import java.time.ZoneOffset;

public class TransactionMapper {

    public static TransactionResponseDTO toDto(Transaction transaction) {
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
