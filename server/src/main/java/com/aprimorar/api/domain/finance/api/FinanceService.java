package com.aprimorar.api.domain.finance.api;

import com.aprimorar.api.domain.registration.employee.api.dto.EmployeeSummaryDTO;
import com.aprimorar.api.domain.finance.api.dto.FinanceSummaryDTO;
import com.aprimorar.api.domain.finance.api.dto.TransactionRequestDTO;
import com.aprimorar.api.domain.finance.api.dto.TransactionResponseDTO;
import com.aprimorar.api.domain.registration.student.api.dto.StudentSummaryDTO;
import com.aprimorar.api.enums.TransactionCategory;
import com.aprimorar.api.enums.TransactionStatus;
import com.aprimorar.api.enums.TransactionType;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FinanceService {

    FinanceSummaryDTO getFinanceSummary();

    StudentSummaryDTO getStudentSummary(UUID studentId, Instant startDate, Instant endDate);

    EmployeeSummaryDTO getEmployeeSummary(UUID employeeId, Instant startDate, Instant endDate);

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
