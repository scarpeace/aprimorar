package com.aprimorar.api.domain.finance;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.event.repository.EventRepository;
import com.aprimorar.api.domain.finance.dto.FinanceSummaryDTO;
import com.aprimorar.api.domain.finance.repository.GeneralExpenseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceService {

    private final EventRepository eventRepository;
    private final GeneralExpenseRepository generalExpenseRepository;

    @Transactional(readOnly = true)
    public FinanceSummaryDTO getFinanceSummary() {
        BigDecimal totalIncome = eventRepository.sumTotalIncome();
        BigDecimal totalIncomePending = eventRepository.sumTotalIncomePending();
        BigDecimal totalExpenseTeacher = eventRepository.sumTotalExpenseTeacher();
        BigDecimal totalExpenseTeacherPending = eventRepository.sumTotalExpenseTeacherPending();
        BigDecimal totalGeneralExpenses = generalExpenseRepository.sumTotalGeneralExpenses();

        BigDecimal balance = totalIncome
            .subtract(totalExpenseTeacher)
            .subtract(totalGeneralExpenses);

        log.info("Resumo financeiro calculado com sucesso.");

        return new FinanceSummaryDTO(
            totalIncome,
            totalIncomePending,
            totalExpenseTeacher,
            totalExpenseTeacherPending,
            totalGeneralExpenses,
            balance
        );
    }
}
