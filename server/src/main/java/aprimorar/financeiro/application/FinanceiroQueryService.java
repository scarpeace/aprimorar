package aprimorar.financeiro.application;

import aprimorar.financeiro.domain.CategoriaDespesaEnum;
import aprimorar.financeiro.FinanceiroQueryApi;
import aprimorar.financeiro.web.dto.DespesaResponseDTO;
import aprimorar.financeiro.web.dto.DespesasResponseDTO;
import aprimorar.financeiro.domain.Despesa;
import aprimorar.financeiro.infrastructure.persistence.DespesaRepository;
import aprimorar.financeiro.infrastructure.persistence.DespesaSpecifications;
import aprimorar.shared.PageDTO;
import aprimorar.shared.exception.BusinessException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinanceiroQueryService implements FinanceiroQueryApi {

    private static final Logger log = LoggerFactory.getLogger(FinanceiroQueryService.class);

    private final DespesaRepository despesaRepository;

    public FinanceiroQueryService(DespesaRepository despesaRepository) {
        this.despesaRepository = despesaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DespesasResponseDTO getDespesas(Pageable pageable, CategoriaDespesaEnum categoria, Instant startDate, Instant endDate) {
        Specification<Despesa> spec = Specification.allOf(
            DespesaSpecifications.withCategory(categoria),
            DespesaSpecifications.withDateFrom(startDate),
            DespesaSpecifications.withDateTo(endDate)
        );

        Specification<Despesa> pendingSpec = Specification.allOf(
            DespesaSpecifications.withCategory(categoria),
            DespesaSpecifications.withDateFrom(startDate),
            DespesaSpecifications.withDateTo(endDate),
            DespesaSpecifications.withPendingPayment()
        );

        PageDTO<DespesaResponseDTO> despesasPage = new PageDTO<>(
            despesaRepository.findAll(spec, pageable).map(DespesaMapper::toDto)
        );
        BigDecimal totalExpenses = despesaRepository.sumBySpecification(spec);
        BigDecimal pendingExpenses = despesaRepository.sumBySpecification(pendingSpec);

        log.info("Consulta de despesas finalizada, {} registros encontrados.", despesasPage.totalElements());

        return new DespesasResponseDTO(
            totalExpenses,
            pendingExpenses,
            despesasPage
        );
    }

    @Override
    @Transactional(readOnly = true)
    public DespesaResponseDTO findDespesaById(UUID id) {
        Despesa despesa = findDespesaOrThrow(id);
        log.info("Despesa {} consultada com sucesso.", despesa.getId());
        return DespesaMapper.toDto(despesa);
    }

    private Despesa findDespesaOrThrow(UUID id) {
        return despesaRepository.findById(id).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Despesa não encontrada"));
    }


}
