package aprimorar.finance.api;

import aprimorar.finance.api.dto.FinanceSummaryDTO;
import aprimorar.finance.api.dto.TransactionRequestDTO;
import aprimorar.finance.api.dto.TransactionResponseDTO;
import aprimorar.finance.api.enums.TransactionCategory;

import aprimorar.finance.api.enums.TransactionStatus;

import aprimorar.finance.api.enums.TransactionType;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FinanceService {

    FinanceSummaryDTO getFinanceSummary();

    TransactionResponseDTO createGeneralExpense(TransactionRequestDTO dto);

    Page<TransactionResponseDTO> findAllGeneralExpenses(
        TransactionCategory category,
        LocalDate startDate,
        LocalDate endDate,
        Pageable pageable
    );

    TransactionResponseDTO findGeneralExpenseById(UUID id);

    TransactionResponseDTO updateGeneralExpense(UUID id, TransactionRequestDTO dto);

    void deleteGeneralExpense(UUID id);

    Page<TransactionResponseDTO> findTransactions(
        TransactionCategory category,
        TransactionType type,
        TransactionStatus status,
        LocalDate startDate,
        LocalDate endDate,
        Pageable pageable
    );
}
