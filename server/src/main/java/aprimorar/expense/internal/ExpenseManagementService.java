package aprimorar.expense.internal;

import aprimorar.expense.api.ExpenseCategory;
import aprimorar.expense.api.dto.ExpenseRequestDTO;
import aprimorar.expense.api.dto.ExpenseResponseDTO;
import aprimorar.expense.api.dto.ExpensesSummaryDTO;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

interface ExpenseManagementService {

    ExpenseResponseDTO createExpense(ExpenseRequestDTO dto);

    ExpensesSummaryDTO getExpenses(ExpenseCategory category, LocalDate startDate, LocalDate endDate, Pageable pageable);

    ExpenseResponseDTO findExpenseById(UUID id);

    ExpenseResponseDTO updateExpense(UUID id, ExpenseRequestDTO dto);

    ExpenseResponseDTO togglePayment(UUID id);

    void deleteExpense(UUID id);
}
