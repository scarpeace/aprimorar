package com.aprimorar.api.domain.finance.internal;

import com.aprimorar.api.domain.finance.api.TransactionService;
import com.aprimorar.api.domain.finance.api.exception.TransactionNotFoundException;
import com.aprimorar.api.domain.finance.internal.repository.TransactionRepository;
import com.aprimorar.api.enums.TransactionCategory;
import com.aprimorar.api.enums.TransactionOrigin;
import com.aprimorar.api.enums.TransactionStatus;
import com.aprimorar.api.enums.TransactionType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Transactional
    public void createEventTransactions(UUID eventId, BigDecimal price, BigDecimal payment) {
        transactionRepository.save(
            new Transaction(
                TransactionType.IN,
                TransactionStatus.PENDING,
                price,
                TransactionOrigin.EVENT_STUDENT_CHARGE,
                eventId,
                null,
                TransactionCategory.COBRANCA_ALUNO
            )
        );
        transactionRepository.save(
            new Transaction(
                TransactionType.OUT,
                TransactionStatus.PENDING,
                payment,
                TransactionOrigin.EVENT_EMPLOYEE_PAYMENT,
                eventId,
                null,
                TransactionCategory.PAGAMENTO_COLABORADOR
            )
        );
    }

    @Transactional
    public void syncEventTransactions(UUID eventId, BigDecimal price, BigDecimal payment) {
        findByOriginAndEventId(TransactionOrigin.EVENT_STUDENT_CHARGE, eventId).setAmount(price);
        findByOriginAndEventId(TransactionOrigin.EVENT_EMPLOYEE_PAYMENT, eventId).setAmount(payment);
    }

    @Transactional
    public void syncStudentCharge(UUID eventId, Instant settledAt) {
        Transaction transaction = findByOriginAndEventId(TransactionOrigin.EVENT_STUDENT_CHARGE, eventId);
        applySettlement(transaction, settledAt);
    }

    @Transactional
    public void syncEmployeePayment(UUID eventId, Instant settledAt) {
        Transaction transaction = findByOriginAndEventId(TransactionOrigin.EVENT_EMPLOYEE_PAYMENT, eventId);
        applySettlement(transaction, settledAt);
    }

    @Transactional
    public void deleteEventTransactions(UUID eventId) {
        transactionRepository.deleteByOriginAndOriginId(TransactionOrigin.EVENT_STUDENT_CHARGE, eventId);
        transactionRepository.deleteByOriginAndOriginId(TransactionOrigin.EVENT_EMPLOYEE_PAYMENT, eventId);
    }

    private Transaction findByOriginAndEventId(TransactionOrigin origin, java.util.UUID eventId) {
        return transactionRepository.findByOriginAndOriginId(origin, eventId)
            .orElseThrow(() -> new TransactionNotFoundException("Transação financeira não encontrada"));
    }

    private void applySettlement(Transaction transaction, Instant settledAt) {
        if (settledAt == null) {
            transaction.reopen();
            return;
        }
        transaction.settle(settledAt);
    }
}
