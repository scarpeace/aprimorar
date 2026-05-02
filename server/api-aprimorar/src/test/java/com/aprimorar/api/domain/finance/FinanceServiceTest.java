package com.aprimorar.api.domain.finance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aprimorar.api.domain.event.repository.EventRepository;
import com.aprimorar.api.domain.finance.dto.FinanceSummaryDTO;
import com.aprimorar.api.domain.finance.repository.GeneralExpenseRepository;

@ExtendWith(MockitoExtension.class)
class FinanceServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private GeneralExpenseRepository generalExpenseRepository;

    @InjectMocks
    private FinanceService financeService;

    @Test
    @DisplayName("should calculate finance summary correctly")
    void shouldCalculateFinanceSummaryCorrectly() {
        when(eventRepository.sumTotalStudentIncome()).thenReturn(new BigDecimal("5000.00"));
        when(eventRepository.sumTotalStudentIncomePending()).thenReturn(new BigDecimal("1200.00"));
        when(eventRepository.sumTotalEmployeePayment()).thenReturn(new BigDecimal("3000.00"));
        when(eventRepository.sumTotalEmployeePaymentPending()).thenReturn(new BigDecimal("800.00"));
        when(generalExpenseRepository.sumTotalGeneralExpenses()).thenReturn(new BigDecimal("500.00"));

        FinanceSummaryDTO actual = financeService.getFinanceSummary();

        assertThat(actual.totalIncome()).isEqualByComparingTo("5000.00");
        assertThat(actual.totalIncomePending()).isEqualByComparingTo("1200.00");
        assertThat(actual.totalExpenseTeacher()).isEqualByComparingTo("3000.00");
        assertThat(actual.totalExpenseTeacherPending()).isEqualByComparingTo("800.00");
        assertThat(actual.totalGeneralExpenses()).isEqualByComparingTo("500.00");
        assertThat(actual.balance()).isEqualByComparingTo("1500.00"); // 5000 - 3000 - 500
    }
}
