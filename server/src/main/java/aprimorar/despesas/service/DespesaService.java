package aprimorar.despesas.service;

import aprimorar.despesas.domain.DespesaEntity;
import aprimorar.despesas.domain.exception.DespesaNaoEncontradaException;
import aprimorar.despesas.repository.DespesaRepository;
import aprimorar.despesas.repository.specifications.DespesaSpecifications;
import aprimorar.despesas.web.dto.DespesaFiltroRequest;
import aprimorar.despesas.web.dto.DespesaRequest;
import aprimorar.despesas.web.dto.DespesaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DespesaService {

    private static final Logger log = LoggerFactory.getLogger(DespesaService.class);

    private final DespesaRepository despesaRepo;

    public DespesaService(DespesaRepository despesaRepo) {
        this.despesaRepo = despesaRepo;
    }

    @Transactional(readOnly = true)
    public Page<DespesaResponse> getDespesas(DespesaFiltroRequest filtro, Pageable pageable) {
        Specification<DespesaEntity> spec = DespesaSpecifications.comFiltros(filtro);
        Page<DespesaEntity> despesasPage = despesaRepo.findAll(spec, pageable);

        log.info("Consulta de despesas finalizada, {} registros encontrados.", despesasPage.getTotalElements());
        return despesasPage.map(DespesaResponse::toDto);
    }

    @Transactional(readOnly = true)
    public DespesaResponse findDespesaById(Long despesaId) {
        DespesaEntity despesa = findDespesaOrThrow(despesaId);
        log.info("Despesa {} consultada com sucesso.", despesa.getTitulo().toUpperCase());
        return DespesaResponse.toDto(despesa);
    }

    @Transactional
    public DespesaResponse createDespesa(DespesaRequest dto) {
        DespesaEntity saved = despesaRepo.save(dto.toEntity());

        log.info("Despesa {} cadastrada com sucesso.", saved.getTitulo().toUpperCase());
        return DespesaResponse.toDto(saved);
    }

    @Transactional
    public DespesaResponse updateDespesa(Long despesaId, DespesaRequest dto) {
        DespesaEntity despesa = findDespesaOrThrow(despesaId);
        DespesaEntity requested = dto.toEntity();

        despesa.update(
            requested.getTitulo(),
            requested.getTipo(),
            requested.getCategoria(),
            requested.getValor(),
            requested.getDataVencimento(),
            requested.getFormaPagamento(),
            requested.getDescricao()
        );

        log.info("Despesa {} atualizada com sucesso.", despesa.getTitulo().toUpperCase());
        return DespesaResponse.toDto(despesa);
    }

    @Transactional
    public void deleteDespesa(Long despesaId) {
        DespesaEntity despesa = findDespesaOrThrow(despesaId);
        despesaRepo.delete(despesa);
        log.info("Despesa {} excluída com sucesso.", despesa.getTitulo().toUpperCase());
    }

    @Transactional
    public DespesaResponse pagar(Long despesaId) {
        DespesaEntity despesa = findDespesaOrThrow(despesaId);

        despesa.pagar();
        log.info("Despesa {} paga com sucesso.", despesa.getTitulo().toUpperCase());
        return DespesaResponse.toDto(despesa);
    }

    @Transactional
    public DespesaResponse cancelarPagamento(Long despesaId) {
        DespesaEntity despesa = findDespesaOrThrow(despesaId);

        despesa.cancelarPagamento();
        log.info("Pagamento da despesa {} cancelado com sucesso.", despesa.getTitulo().toUpperCase());
        return DespesaResponse.toDto(despesa);
    }

    private DespesaEntity findDespesaOrThrow(Long despesaId) {
        return despesaRepo.findById(despesaId)
            .orElseThrow(DespesaNaoEncontradaException::new);
    }
}
