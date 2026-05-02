package com.aprimorar.api.domain.finance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.event.repository.EventRepository;
import com.aprimorar.api.domain.finance.dto.FinanceSummaryDTO;
import com.aprimorar.api.domain.finance.dto.GeneralExpenseRequestDTO;
import com.aprimorar.api.domain.finance.dto.GeneralExpenseResponseDTO;
import com.aprimorar.api.domain.finance.exception.GeneralExpenseNotFoundException;
import com.aprimorar.api.domain.finance.repository.GeneralExpenseRepository;
import com.aprimorar.api.enums.GeneralExpenseCategory;

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
        BigDecimal totalIncome = eventRepository.sumTotalStudentIncome();
        BigDecimal totalIncomePending = eventRepository.sumTotalStudentIncomePending();
        BigDecimal totalExpenseTeacher = eventRepository.sumTotalEmployeePayment();
        BigDecimal totalExpenseTeacherPending = eventRepository.sumTotalEmployeePaymentPending();
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

    @Transactional
    public GeneralExpenseResponseDTO createGeneralExpense(GeneralExpenseRequestDTO dto) {
        GeneralExpense expense = new GeneralExpense(
                dto.description(),
                dto.amount(),
                dto.date(),
                dto.category()
        );
        return GeneralExpenseResponseDTO.fromEntity(generalExpenseRepository.save(expense));
    }

    @Transactional(readOnly = true)
    public Page<GeneralExpenseResponseDTO> findAllGeneralExpenses(
            GeneralExpenseCategory category,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable) {
        return generalExpenseRepository.findAllWithFilters(category, startDate, endDate, pageable)
                .map(GeneralExpenseResponseDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public GeneralExpenseResponseDTO findGeneralExpenseById(UUID id) {
        return generalExpenseRepository.findById(id)
                .map(GeneralExpenseResponseDTO::fromEntity)
                .orElseThrow(() -> new GeneralExpenseNotFoundException("Despesa geral não encontrada"));
    }

    @Transactional
    public GeneralExpenseResponseDTO updateGeneralExpense(UUID id, GeneralExpenseRequestDTO dto) {
        GeneralExpense expense = generalExpenseRepository.findById(id)
                .orElseThrow(() -> new GeneralExpenseNotFoundException("Despesa geral não encontrada"));

        expense.setDescription(dto.description());
        expense.setAmount(dto.amount());
        expense.setDate(dto.date());
        expense.setCategory(dto.category());

        return GeneralExpenseResponseDTO.fromEntity(generalExpenseRepository.save(expense));
    }

    @Transactional
    public void deleteGeneralExpense(UUID id) {
        if (!generalExpenseRepository.existsById(id)) {
            throw new GeneralExpenseNotFoundException("Despesa geral não encontrada");
        }
        generalExpenseRepository.deleteById(id);
    }
}
