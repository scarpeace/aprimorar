package aprimorar.expense.internal;

import aprimorar.expense.api.ExpenseCategory;
import aprimorar.expense.api.ExpenseService;
import aprimorar.expense.api.dto.ExpenseRequestDTO;
import aprimorar.expense.api.dto.ExpenseResponseDTO;
import aprimorar.expense.api.exception.ExpenseNotFoundException;
import aprimorar.shared.PageDTO;
import java.math.BigDecimal;
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

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    @Transactional
    public ExpenseResponseDTO createExpense(ExpenseRequestDTO dto) {
        Expense expense = new Expense(dto.amount(), dto.date(), dto.category(), dto.description());
        return toDto(expenseRepository.save(expense));
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO<ExpenseResponseDTO> getExpenses(ExpenseCategory category, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<ExpenseResponseDTO> page = expenseRepository.findFiltered(category, startDate, endDate, pageable).map(this::toDto);
        return new PageDTO<>(page);
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponseDTO findExpenseById(UUID id) {
        return toDto(findExpenseOrThrow(id));
    }

    @Override
    @Transactional
    public ExpenseResponseDTO updateExpense(UUID id, ExpenseRequestDTO dto) {
        Expense expense = findExpenseOrThrow(id);
        expense.update(dto.amount(), dto.date(), dto.category(), dto.description());
        return toDto(expenseRepository.save(expense));
    }

    @Override
    @Transactional
    public void deleteExpense(UUID id) {
        expenseRepository.delete(findExpenseOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal sumExpenses(Instant startDate, Instant endDate) {
        return expenseRepository.sumFiltered(toUtcLocalDate(startDate), toUtcLocalDate(endDate));
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
            expense.getDescription()
        );
    }

    private LocalDate toUtcLocalDate(Instant instant) {
        return instant == null ? null : instant.atZone(ZoneOffset.UTC).toLocalDate();
    }
}
