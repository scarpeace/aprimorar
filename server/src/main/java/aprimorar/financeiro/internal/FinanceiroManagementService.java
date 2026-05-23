package aprimorar.financeiro.internal;

import aprimorar.financeiro.api.CategoriaDespesa;
import aprimorar.financeiro.api.dto.DespesaRequestDTO;
import aprimorar.financeiro.api.dto.DespesaResponseDTO;
import aprimorar.financeiro.api.dto.ResumoDespesasDTO;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

interface FinanceiroManagementService {

    DespesaResponseDTO createExpense(DespesaRequestDTO dto);

    ResumoDespesasDTO getExpenses(CategoriaDespesa category, LocalDate startDate, LocalDate endDate, Pageable pageable);

    DespesaResponseDTO findExpenseById(UUID id);

    DespesaResponseDTO updateExpense(UUID id, DespesaRequestDTO dto);

    DespesaResponseDTO togglePayment(UUID id);

    void deleteExpense(UUID id);
}
