package com.aprimorar.api.domain.finance.api;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public interface TransactionService {

    void createEventTransactions(UUID eventId, BigDecimal price, BigDecimal payment);

    void syncEventTransactions(UUID eventId, BigDecimal price, BigDecimal payment);

    void syncStudentCharge(UUID eventId, Instant settledAt);

    void syncEmployeePayment(UUID eventId, Instant settledAt);

    void deleteEventTransactions(UUID eventId);
}
