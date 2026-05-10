package aprimorar.finance;

import aprimorar.finance.internal.FinanceServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import aprimorar.finance.api.dto.FinanceSummaryDTO;
import aprimorar.finance.internal.repository.TransactionRepository;
import aprimorar.student.internal.repository.StudentRepository;
import aprimorar.employee.internal.repository.EmployeeRepository;
import aprimorar.shared.enums.TransactionCategory;
import aprimorar.shared.enums.TransactionOrigin;
import aprimorar.shared.enums.TransactionStatus;
import aprimorar.shared.enums.TransactionType;

@ExtendWith(MockitoExtension.class)
class FinanceServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private FinanceServiceImpl financeService;

    @Test
    @DisplayName("should calculate finance summary correctly")
    void shouldCalculateFinanceSummaryCorrectly() {
        when(transactionRepository.sumByTypeStatusAndCategory(
            TransactionType.IN,
            TransactionStatus.SETTLED,
            TransactionCategory.COBRANCA_ALUNO
        )).thenReturn(new BigDecimal("5000.00"));
        when(transactionRepository.sumByTypeStatusAndCategory(
            TransactionType.IN,
            TransactionStatus.PENDING,
            TransactionCategory.COBRANCA_ALUNO
        )).thenReturn(new BigDecimal("1200.00"));
        when(transactionRepository.sumByTypeStatusAndCategory(
            TransactionType.OUT,
            TransactionStatus.SETTLED,
            TransactionCategory.PAGAMENTO_COLABORADOR
        )).thenReturn(new BigDecimal("3000.00"));
        when(transactionRepository.sumByTypeStatusAndCategory(
            TransactionType.OUT,
            TransactionStatus.PENDING,
            TransactionCategory.PAGAMENTO_COLABORADOR
        )).thenReturn(new BigDecimal("800.00"));
        when(transactionRepository.sumByOriginAndStatus(
            TransactionOrigin.GENERAL_EXPENSE,
            TransactionStatus.SETTLED
        )).thenReturn(new BigDecimal("500.00"));

        FinanceSummaryDTO actual = financeService.getFinanceSummary();

        assertThat(actual.totalIncome()).isEqualByComparingTo("5000.00");
        assertThat(actual.totalIncomePending()).isEqualByComparingTo("1200.00");
        assertThat(actual.totalExpenseTeacher()).isEqualByComparingTo("3000.00");
        assertThat(actual.totalExpenseTeacherPending()).isEqualByComparingTo("800.00");
        assertThat(actual.totalGeneralExpenses()).isEqualByComparingTo("500.00");
        assertThat(actual.balance()).isEqualByComparingTo("1500.00"); // 5000 - 3000 - 500
    }
}
