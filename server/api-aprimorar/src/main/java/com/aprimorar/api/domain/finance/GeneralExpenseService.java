package com.aprimorar.api.domain.finance;

import com.aprimorar.api.domain.finance.dto.GeneralExpenseRequestDTO;
import com.aprimorar.api.domain.finance.dto.GeneralExpenseResponseDTO;
import com.aprimorar.api.domain.finance.exception.GeneralExpenseNotFoundException;
import com.aprimorar.api.domain.finance.repository.GeneralExpenseRepository;
import com.aprimorar.api.enums.GeneralExpenseCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class GeneralExpenseService {

    private final GeneralExpenseRepository repository;

    public GeneralExpenseService(GeneralExpenseRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public GeneralExpenseResponseDTO create(GeneralExpenseRequestDTO dto) {
        GeneralExpense expense = new GeneralExpense(
                dto.description(),
                dto.amount(),
                dto.date(),
                dto.category()
        );
        return GeneralExpenseResponseDTO.fromEntity(repository.save(expense));
    }

    @Transactional(readOnly = true)
    public Page<GeneralExpenseResponseDTO> findAll(
            GeneralExpenseCategory category,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable) {
        return repository.findAllWithFilters(category, startDate, endDate, pageable)
                .map(GeneralExpenseResponseDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public GeneralExpenseResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(GeneralExpenseResponseDTO::fromEntity)
                .orElseThrow(() -> new GeneralExpenseNotFoundException("Despesa geral não encontrada"));
    }

    @Transactional
    public GeneralExpenseResponseDTO update(UUID id, GeneralExpenseRequestDTO dto) {
        GeneralExpense expense = repository.findById(id)
                .orElseThrow(() -> new GeneralExpenseNotFoundException("Despesa geral não encontrada"));

        expense.setDescription(dto.description());
        expense.setAmount(dto.amount());
        expense.setDate(dto.date());
        expense.setCategory(dto.category());

        return GeneralExpenseResponseDTO.fromEntity(repository.save(expense));
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new GeneralExpenseNotFoundException("Despesa geral não encontrada");
        }
        repository.deleteById(id);
    }
}
