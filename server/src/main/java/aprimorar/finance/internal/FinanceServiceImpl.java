package aprimorar.finance.internal;

import aprimorar.finance.api.FinanceService;
import aprimorar.finance.api.dto.FinanceSummaryDTO;
import aprimorar.finance.api.dto.TransactionRequestDTO;
import aprimorar.finance.api.dto.TransactionResponseDTO;
import aprimorar.finance.api.exception.TransactionNotFoundException;
import aprimorar.finance.internal.repository.TransactionRepository;
import aprimorar.finance.internal.repository.TransactionSpecifications;
import aprimorar.finance.api.enums.TransactionCategory;

import aprimorar.finance.api.enums.TransactionOrigin;

import aprimorar.finance.api.enums.TransactionStatus;

import aprimorar.finance.api.enums.TransactionType;
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
public class FinanceServiceImpl implements FinanceService {

    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public FinanceSummaryDTO getFinanceSummary() {
        BigDecimal totalStudentCharged = transactionRepository.sumByTypeStatusAndCategory(
            TransactionType.IN,
            TransactionStatus.SETTLED,
            TransactionCategory.COBRANCA_ALUNO
        );
        BigDecimal totalStudentPending = transactionRepository.sumByTypeStatusAndCategory(
            TransactionType.IN,
            TransactionStatus.PENDING,
            TransactionCategory.COBRANCA_ALUNO
        );
        BigDecimal totalEmployeePaid = transactionRepository.sumByTypeStatusAndCategory(
            TransactionType.OUT,
            TransactionStatus.SETTLED,
            TransactionCategory.PAGAMENTO_COLABORADOR
        );
        BigDecimal totalEmployeePending = transactionRepository.sumByTypeStatusAndCategory(
            TransactionType.OUT,
            TransactionStatus.PENDING,
            TransactionCategory.PAGAMENTO_COLABORADOR
        );
        BigDecimal totalGeneralExpenses = transactionRepository.sumByOriginAndStatus(
            TransactionOrigin.GENERAL_EXPENSE,
            TransactionStatus.SETTLED
        );

        BigDecimal balance = totalStudentCharged
            .subtract(totalEmployeePaid)
            .subtract(totalGeneralExpenses);

        return new FinanceSummaryDTO(
            totalStudentCharged,
            totalStudentPending,
            totalEmployeePaid,
            totalEmployeePending,
            totalGeneralExpenses,
            balance
        );
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
        return TransactionMapper.toDto(transactionRepository.save(saved));
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
            .map(TransactionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public TransactionResponseDTO findGeneralExpenseById(UUID id) {
        Transaction transaction = findGeneralExpenseTransaction(id);
        return TransactionMapper.toDto(transaction);
    }

    @Transactional
    public TransactionResponseDTO updateGeneralExpense(UUID id, TransactionRequestDTO dto) {
        Transaction transaction = findGeneralExpenseTransaction(id);
        transaction.setAmount(dto.amount());
        transaction.setCategory(dto.category());
        transaction.setSettledAt(toExpenseInstant(dto.date()));
        transaction.settle(transaction.getSettledAt());
        return TransactionMapper.toDto(transactionRepository.save(transaction));
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
            .map(TransactionMapper::toDto);
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
