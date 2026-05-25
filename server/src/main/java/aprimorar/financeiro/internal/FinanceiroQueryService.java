package aprimorar.financeiro.internal;

import aprimorar.financeiro.api.FinanceiroQueryApi;
import aprimorar.financeiro.api.dto.DespesaResponseDTO;
import aprimorar.financeiro.api.dto.DespesasResponseDTO;
import aprimorar.financeiro.internal.repository.DespesaRepository;
import aprimorar.financeiro.internal.repository.DespesaSpecifications;
import aprimorar.shared.PageDTO;
import aprimorar.shared.exception.BusinessException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinanceiroQueryService implements FinanceiroQueryApi {

    private final DespesaRepository despesaRepository;

    public FinanceiroQueryService(DespesaRepository despesaRepository) {
        this.despesaRepository = despesaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DespesasResponseDTO getDespesas(Pageable pageable, CategoriaDespesaEnum category, LocalDate startDate, LocalDate endDate) {
        Specification<Despesa> spec = Specification.allOf(
            DespesaSpecifications.withCategory(category),
            DespesaSpecifications.withDateFrom(startDate),
            DespesaSpecifications.withDateTo(endDate)
        );

        Specification<Despesa> pendingSpec = Specification.allOf(
            DespesaSpecifications.withCategory(category),
            DespesaSpecifications.withDateFrom(startDate),
            DespesaSpecifications.withDateTo(endDate),
            DespesaSpecifications.withPendingPayment()
        );

        PageDTO<DespesaResponseDTO> despesasPage = new PageDTO<>(
            despesaRepository.findAll(spec, pageable).map(DespesaMapper::toDto)
        );
        BigDecimal totalExpenses = despesaRepository.sumBySpecification(spec);
        BigDecimal pendingExpenses = despesaRepository.sumBySpecification(pendingSpec);

        return new DespesasResponseDTO(
            totalExpenses,
            pendingExpenses,
            despesasPage
        );
    }

    @Override
    @Transactional(readOnly = true)
    public DespesaResponseDTO findDespesaById(UUID id) {
        return DespesaMapper.toDto(findExpenseOrThrow(id));
    }

    private Despesa findExpenseOrThrow(UUID id) {
        return despesaRepository.findById(id).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Despesa não encontrada"));
    }


}
