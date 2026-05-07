package com.aprimorar.api.domain.finance;

import com.aprimorar.api.domain.employee.dto.EmployeeSummaryDTO;
import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;
import com.aprimorar.api.domain.employee.repository.EmployeeRepository;
import com.aprimorar.api.domain.event.repository.EventRepository;
import com.aprimorar.api.domain.finance.dto.FinanceSummaryDTO;
import com.aprimorar.api.domain.finance.dto.TransactionRequestDTO;
import com.aprimorar.api.domain.finance.dto.TransactionResponseDTO;
import com.aprimorar.api.domain.finance.exception.TransactionNotFoundException;
import com.aprimorar.api.domain.finance.repository.TransactionRepository;
import com.aprimorar.api.domain.finance.repository.TransactionSpecifications;
import com.aprimorar.api.domain.student.dto.StudentSummaryDTO;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;
import com.aprimorar.api.domain.student.repository.StudentRepository;
import com.aprimorar.api.enums.TransactionCategory;
import com.aprimorar.api.enums.TransactionOrigin;
import com.aprimorar.api.enums.TransactionStatus;
import com.aprimorar.api.enums.TransactionType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceService {

    private final EventRepository eventRepository;
    private final TransactionRepository transactionRepository;
    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public FinanceSummaryDTO getFinanceSummary() {
        BigDecimal totalIncome = transactionRepository.sumByTypeStatusAndCategory(
            TransactionType.IN,
            TransactionStatus.SETTLED,
            TransactionCategory.COBRANCA_ALUNO
        );
        BigDecimal totalIncomePending = transactionRepository.sumByTypeStatusAndCategory(
            TransactionType.IN,
            TransactionStatus.PENDING,
            TransactionCategory.COBRANCA_ALUNO
        );
        BigDecimal totalExpenseTeacher = transactionRepository.sumByTypeStatusAndCategory(
            TransactionType.OUT,
            TransactionStatus.SETTLED,
            TransactionCategory.PAGAMENTO_COLABORADOR
        );
        BigDecimal totalExpenseTeacherPending = transactionRepository.sumByTypeStatusAndCategory(
            TransactionType.OUT,
            TransactionStatus.PENDING,
            TransactionCategory.PAGAMENTO_COLABORADOR
        );
        BigDecimal totalGeneralExpenses = transactionRepository.sumByOriginAndStatus(
            TransactionOrigin.GENERAL_EXPENSE,
            TransactionStatus.SETTLED
        );

        BigDecimal balance = totalIncome
            .subtract(totalExpenseTeacher)
            .subtract(totalGeneralExpenses);

        return new FinanceSummaryDTO(
            totalIncome,
            totalIncomePending,
            totalExpenseTeacher,
            totalExpenseTeacherPending,
            totalGeneralExpenses,
            balance
        );
    }

    @Transactional(readOnly = true)
    public StudentSummaryDTO getStudentSummary(UUID studentId, Instant startDate, Instant endDate) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException("Aluno com o ID informado não encontrado");
        }

        long totalEvents = startDate == null || endDate == null
            ? eventRepository.countByStudentId(studentId)
            : eventRepository.countByStudentIdAndStartDateBetween(studentId, startDate, endDate);

        BigDecimal totalCharged = transactionRepository.sumStudentTransactions(
            TransactionOrigin.EVENT_STUDENT_CHARGE,
            studentId,
            startDate,
            endDate,
            TransactionStatus.SETTLED
        );
        BigDecimal totalPending = transactionRepository.sumStudentTransactions(
            TransactionOrigin.EVENT_STUDENT_CHARGE,
            studentId,
            startDate,
            endDate,
            TransactionStatus.PENDING
        );

        return new StudentSummaryDTO(totalEvents, totalCharged, totalPending);
    }

    @Transactional(readOnly = true)
    public EmployeeSummaryDTO getEmployeeSummary(UUID employeeId, Instant startDate, Instant endDate) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Colaborador com o ID informado não encontrado");
        }

        long totalEvents = startDate == null || endDate == null
            ? eventRepository.countByEmployeeId(employeeId)
            : eventRepository.countByEmployeeIdAndStartDateBetween(employeeId, startDate, endDate);

        BigDecimal totalPaid = transactionRepository.sumEmployeeTransactions(
            TransactionOrigin.EVENT_EMPLOYEE_PAYMENT,
            employeeId,
            startDate,
            endDate,
            TransactionStatus.SETTLED
        );
        BigDecimal totalUnpaid = transactionRepository.sumEmployeeTransactions(
            TransactionOrigin.EVENT_EMPLOYEE_PAYMENT,
            employeeId,
            startDate,
            endDate,
            TransactionStatus.PENDING
        );

        return new EmployeeSummaryDTO(totalEvents, totalPaid, totalUnpaid);
    }

    @Transactional
    public TransactionResponseDTO createGeneralExpense(TransactionRequestDTO dto) {
        Transaction transaction = new Transaction(
            TransactionType.OUT,
            TransactionStatus.SETTLED,
            dto.amount(),
            TransactionOrigin.GENERAL_EXPENSE,
            UUID.randomUUID(),
            toExpenseInstant(dto.date()),
            dto.category()
        );
        Transaction saved = transactionRepository.save(transaction);
        saved.setOriginId(saved.getId());
        return TransactionResponseDTO.fromEntity(transactionRepository.save(saved));
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDTO> findAllGeneralExpenses(
        TransactionCategory category,
        LocalDate startDate,
        LocalDate endDate,
        Pageable pageable
    ) {
        Instant start = startDate == null ? null : startDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
        Instant end = endDate == null ? null : endDate.plusDays(1).atStartOfDay(ZoneId.of("UTC")).toInstant().minusNanos(1);

        Specification<Transaction> spec = Specification
            .where(TransactionSpecifications.withOrigin(TransactionOrigin.GENERAL_EXPENSE))
            .and(TransactionSpecifications.withCategory(category))
            .and(TransactionSpecifications.withSettledAtAfter(start))
            .and(TransactionSpecifications.withSettledAtBefore(end));

        return transactionRepository.findAll(spec, pageable)
            .map(TransactionResponseDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public TransactionResponseDTO findGeneralExpenseById(UUID id) {
        Transaction transaction = findGeneralExpenseTransaction(id);
        return TransactionResponseDTO.fromEntity(transaction);
    }

    @Transactional
    public TransactionResponseDTO updateGeneralExpense(UUID id, TransactionRequestDTO dto) {
        Transaction transaction = findGeneralExpenseTransaction(id);
        transaction.setAmount(dto.amount());
        transaction.setCategory(dto.category());
        transaction.setSettledAt(toExpenseInstant(dto.date()));
        transaction.settle(transaction.getSettledAt());
        return TransactionResponseDTO.fromEntity(transactionRepository.save(transaction));
    }

    @Transactional
    public void deleteGeneralExpense(UUID id) {
        Transaction transaction = findGeneralExpenseTransaction(id);
        transactionRepository.delete(transaction);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDTO> findTransactions(
        TransactionCategory category,
        TransactionType type,
        TransactionStatus status,
        LocalDate startDate,
        LocalDate endDate,
        Pageable pageable
    ) {
        Instant start = startDate == null ? null : startDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
        Instant end = endDate == null ? null : endDate.plusDays(1).atStartOfDay(ZoneId.of("UTC")).toInstant().minusNanos(1);

        Specification<Transaction> spec = Specification
            .where(TransactionSpecifications.withCategory(category))
            .and(TransactionSpecifications.withType(type))
            .and(TransactionSpecifications.withStatus(status))
            .and(TransactionSpecifications.withSettledAtAfter(start))
            .and(TransactionSpecifications.withSettledAtBefore(end));

        return transactionRepository.findAll(spec, pageable)
            .map(TransactionResponseDTO::fromEntity);
    }

    private Transaction findGeneralExpenseTransaction(UUID id) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new TransactionNotFoundException("Despesa geral não encontrada"));
        if (transaction.getOrigin() != TransactionOrigin.GENERAL_EXPENSE) {
            throw new TransactionNotFoundException("Despesa geral não encontrada");
        }
        return transaction;
    }

    private Instant toExpenseInstant(LocalDate date) {
        return date.atStartOfDay(ZoneId.of("UTC")).toInstant();
    }
}
