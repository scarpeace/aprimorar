package com.aprimorar.api.domain.finance;

import com.aprimorar.api.domain.event.Event;
import com.aprimorar.api.domain.finance.exception.TransactionNotFoundException;
import com.aprimorar.api.domain.finance.repository.TransactionRepository;
import com.aprimorar.api.enums.TransactionCategory;
import com.aprimorar.api.enums.TransactionOrigin;
import com.aprimorar.api.enums.TransactionStatus;
import com.aprimorar.api.enums.TransactionType;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Transactional
    public void createEventTransactions(Event event) {
        transactionRepository.save(
            new Transaction(
                TransactionType.IN,
                TransactionStatus.PENDING,
                event.getPrice(),
                TransactionOrigin.EVENT_STUDENT_CHARGE,
                event.getId(),
                null,
                TransactionCategory.COBRANCA_ALUNO
            )
        );
        transactionRepository.save(
            new Transaction(
                TransactionType.OUT,
                TransactionStatus.PENDING,
                event.getPayment(),
                TransactionOrigin.EVENT_EMPLOYEE_PAYMENT,
                event.getId(),
                null,
                TransactionCategory.PAGAMENTO_COLABORADOR
            )
        );
    }

    @Transactional
    public void syncEventTransactions(Event event) {
        findByOriginAndEventId(TransactionOrigin.EVENT_STUDENT_CHARGE, event.getId()).setAmount(event.getPrice());
        findByOriginAndEventId(TransactionOrigin.EVENT_EMPLOYEE_PAYMENT, event.getId()).setAmount(event.getPayment());
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
