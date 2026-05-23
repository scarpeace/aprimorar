package aprimorar.expense.internal;

import aprimorar.expense.api.ExpenseCategory;
import aprimorar.expense.api.ExpenseService;
import aprimorar.expense.api.dto.ExpenseRequestDTO;
import aprimorar.expense.api.dto.ExpenseResponseDTO;
import aprimorar.expense.api.dto.ExpensesSummaryDTO;
import aprimorar.expense.api.exception.ExpenseNotFoundException;
import aprimorar.shared.PageDTO;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final Clock clock;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, Clock clock) {
        this.expenseRepository = expenseRepository;
        this.clock = clock;
    }

    @Transactional
    public ExpenseResponseDTO createExpense(ExpenseRequestDTO dto) {
        Expense expense = new Expense(dto.amount(), dto.date(), dto.category(), dto.description());
        return toDto(expenseRepository.save(expense));
    }

    @Transactional(readOnly = true)
    public ExpensesSummaryDTO getExpenses(ExpenseCategory category, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<ExpenseResponseDTO> page = expenseRepository.findFiltered(category, startDate, endDate, pageable).map(this::toDto);
        return new ExpensesSummaryDTO(
            new PageDTO<>(page),
            expenseRepository.sumFiltered(startDate, endDate),
            expenseRepository.sumPendingFiltered(startDate, endDate)
        );
    }

    @Transactional(readOnly = true)
    public ExpenseResponseDTO findExpenseById(UUID id) {
        return toDto(findExpenseOrThrow(id));
    }

    @Transactional
    public ExpenseResponseDTO updateExpense(UUID id, ExpenseRequestDTO dto) {
        Expense expense = findExpenseOrThrow(id);
        expense.update(dto.amount(), dto.date(), dto.category(), dto.description());
        return toDto(expenseRepository.save(expense));
    }

    @Transactional
    public ExpenseResponseDTO togglePayment(UUID id) {
        Expense expense = findExpenseOrThrow(id);
        expense.togglePayment(Instant.now(clock));
        return toDto(expense);
    }

    @Transactional
    public void deleteExpense(UUID id) {
        expenseRepository.delete(findExpenseOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal sumExpenses(Instant startDate, Instant endDate) {
        return expenseRepository.sumFiltered(toUtcLocalDate(startDate), toUtcLocalDate(endDate));
    }

    @Transactional(readOnly = true)
    public BigDecimal sumPendingExpenses(Instant startDate, Instant endDate) {
        return expenseRepository.sumPendingFiltered(toUtcLocalDate(startDate), toUtcLocalDate(endDate));
    }

    private Expense findExpenseOrThrow(UUID id) {
        return expenseRepository.findById(id).orElseThrow(ExpenseNotFoundException::new);
    }

    private ExpenseResponseDTO toDto(Expense expense) {
        return new ExpenseResponseDTO(
            expense.getId(),
            expense.getAmount(),
            expense.getDate(),
            expense.getCategory(),
            expense.getDescription(),
            expense.getPaymentDate()
        );
    }

    private LocalDate toUtcLocalDate(Instant instant) {
        return instant == null ? null : instant.atZone(ZoneOffset.UTC).toLocalDate();
    }
}
