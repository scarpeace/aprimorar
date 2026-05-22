package aprimorar.expense.api;

import aprimorar.expense.api.dto.ExpenseRequestDTO;
import aprimorar.expense.api.dto.ExpenseResponseDTO;
import aprimorar.expense.api.dto.ExpensesSummaryDTO;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface ExpenseService {

    ExpenseResponseDTO createExpense(ExpenseRequestDTO dto);

    ExpensesSummaryDTO getExpenses(ExpenseCategory category, LocalDate startDate, LocalDate endDate, Pageable pageable);

    ExpenseResponseDTO findExpenseById(UUID id);

    ExpenseResponseDTO updateExpense(UUID id, ExpenseRequestDTO dto);

    ExpenseResponseDTO togglePayment(UUID id);

    void deleteExpense(UUID id);

    BigDecimal sumExpenses(Instant startDate, Instant endDate);

    BigDecimal sumPendingExpenses(Instant startDate, Instant endDate);
}
