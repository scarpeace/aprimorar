package aprimorar.despesas.service;

import aprimorar.despesas.domain.Despesa;
import aprimorar.despesas.dto.DespesaFiltroRequest;
import aprimorar.despesas.dto.DespesaResponse;
import aprimorar.despesas.repository.DespesaRepository;
import aprimorar.despesas.repository.specifications.DespesaSpecifications;
import aprimorar.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DespesaQueryService {

    private static final Logger log = LoggerFactory.getLogger(DespesaQueryService.class);

    private final DespesaRepository despesaRepo;

    public DespesaQueryService(DespesaRepository despesaRepo) {
        this.despesaRepo = despesaRepo;
    }

    @Transactional(readOnly = true)
    public Page<DespesaResponse> getDespesas(DespesaFiltroRequest filtro, Pageable pageable) {
        Specification<Despesa> spec = DespesaSpecifications.comFiltros(filtro);
        Page<Despesa> despesasPage = despesaRepo.findAll(spec, pageable);

        log.info("Consulta de despesas finalizada, {} registros encontrados.", despesasPage.getTotalElements());
        return despesasPage.map(DespesaResponse::toDto);
    }

    @Transactional(readOnly = true)
    public DespesaResponse findDespesaById(Long despesaId) {
        Despesa despesa = findDespesaOrThrow(despesaId);
        log.info("Despesa {} consultada com sucesso.", despesa.getTitulo().toUpperCase());
        return DespesaResponse.toDto(despesa);
    }

    private Despesa findDespesaOrThrow(Long despesaId) {
        return despesaRepo
            .findById(despesaId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Despesa não encontrada no banco de dados"));
    }
}
