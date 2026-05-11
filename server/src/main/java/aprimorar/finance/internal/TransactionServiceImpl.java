package aprimorar.finance.internal;

import aprimorar.finance.api.TransactionService;
import aprimorar.finance.api.exception.TransactionNotFoundException;
import aprimorar.finance.internal.repository.TransactionRepository;
import aprimorar.finance.api.enums.TransactionCategory;

import aprimorar.finance.api.enums.TransactionOrigin;

import aprimorar.finance.api.enums.TransactionStatus;

import aprimorar.finance.api.enums.TransactionType;
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
    public void createAppointmentTransactions(UUID appointmentId, BigDecimal price, BigDecimal payment) {
        transactionRepository.save(
            new Transaction(
                TransactionType.IN,
                TransactionStatus.PENDING,
                price,
                TransactionOrigin.APPOINTMENT_STUDENT_CHARGE,
                appointmentId,
                null,
                TransactionCategory.COBRANCA_ALUNO
            )
        );
        transactionRepository.save(
            new Transaction(
                TransactionType.OUT,
                TransactionStatus.PENDING,
                payment,
                TransactionOrigin.APPOINTMENT_EMPLOYEE_PAYMENT,
                appointmentId,
                null,
                TransactionCategory.PAGAMENTO_COLABORADOR
            )
        );
    }

    @Transactional
    public void syncAppointmentTransactions(UUID appointmentId, BigDecimal price, BigDecimal payment) {
        findByOriginAndAppointmentId(TransactionOrigin.APPOINTMENT_STUDENT_CHARGE, appointmentId).setAmount(price);
        findByOriginAndAppointmentId(TransactionOrigin.APPOINTMENT_EMPLOYEE_PAYMENT, appointmentId).setAmount(payment);
    }

    @Transactional
    public void syncStudentCharge(UUID appointmentId, Instant settledAt) {
        Transaction transaction = findByOriginAndAppointmentId(TransactionOrigin.APPOINTMENT_STUDENT_CHARGE, appointmentId);
        applySettlement(transaction, settledAt);
    }

    @Transactional
    public void syncEmployeePayment(UUID appointmentId, Instant settledAt) {
        Transaction transaction = findByOriginAndAppointmentId(TransactionOrigin.APPOINTMENT_EMPLOYEE_PAYMENT, appointmentId);
        applySettlement(transaction, settledAt);
    }   

    @Transactional
    public void deleteAppointmentTransactions(UUID appointmentId) {
        transactionRepository.deleteByOriginAndOriginId(TransactionOrigin.APPOINTMENT_STUDENT_CHARGE, appointmentId);
        transactionRepository.deleteByOriginAndOriginId(TransactionOrigin.APPOINTMENT_EMPLOYEE_PAYMENT, appointmentId);
    }

    private Transaction findByOriginAndAppointmentId(TransactionOrigin origin, java.util.UUID appointmentId) {
        return transactionRepository.findByOriginAndOriginId(origin, appointmentId)
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
