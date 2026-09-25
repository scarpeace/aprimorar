package aprimorar.financeiro.operacional.service;

import java.time.LocalDate;

import aprimorar.financeiro.operacional.domain.Despesa;
import aprimorar.financeiro.operacional.domain.enums.StatusDespesa;
import aprimorar.financeiro.operacional.domain.exception.DespesaNaoEncontradaException;
import aprimorar.financeiro.operacional.repository.DespesaRepository;
import aprimorar.financeiro.operacional.repository.DespesaSpecifications;
import aprimorar.financeiro.operacional.web.dto.DespesaFiltroRequest;
import aprimorar.financeiro.operacional.web.dto.DespesaRequest;
import aprimorar.financeiro.operacional.web.dto.DespesaResponse;

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

    @Transactional
    public DespesaResponse createDespesa(DespesaRequest dto) {
        Despesa saved = despesaRepo.save(dto.toEntity());

        log.info("Despesa {} cadastrada com sucesso.", saved.getTitulo().toUpperCase());
        return DespesaResponse.toDto(saved);
    }

    @Transactional
    public DespesaResponse updateDespesa(Long despesaId, DespesaRequest dto) {
        Despesa despesa = findDespesaOrThrow(despesaId);
        Despesa requested = dto.toEntity();

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
        Despesa despesa = findDespesaOrThrow(despesaId);
        despesaRepo.delete(despesa);
        log.info("Despesa {} excluída com sucesso.", despesa.getTitulo().toUpperCase());
    }

    @Transactional
    public DespesaResponse pagar(Long despesaId) {
        Despesa despesa = findDespesaOrThrow(despesaId);

        despesa.pagar();
        log.info("Despesa {} paga com sucesso.", despesa.getTitulo().toUpperCase());
        return DespesaResponse.toDto(despesa);
    }

    @Transactional
    public DespesaResponse cancelarPagamento(Long despesaId) {
        Despesa despesa = findDespesaOrThrow(despesaId);

        despesa.cancelarPagamento();
        log.info("Pagamento da despesa {} cancelado com sucesso.", despesa.getTitulo().toUpperCase());
        return DespesaResponse.toDto(despesa);
    }

    @Transactional
    public int atualizarStatusAtrasos() {
        LocalDate hoje = LocalDate.now();
        int reabertas = despesaRepo.reabrirVencimentosFuturos(
            StatusDespesa.ATRASADA,
            StatusDespesa.PENDENTE,
            hoje
        );
        int atrasadas = despesaRepo.marcarAtrasadas(
            StatusDespesa.PENDENTE,
            StatusDespesa.ATRASADA,
            hoje
        );
        return reabertas + atrasadas;
    }

    private Despesa findDespesaOrThrow(Long despesaId) {
        return despesaRepo.findById(despesaId)
            .orElseThrow(DespesaNaoEncontradaException::new);
    }
}
