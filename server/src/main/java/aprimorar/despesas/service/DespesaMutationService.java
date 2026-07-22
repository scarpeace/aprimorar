package aprimorar.despesas.service;

import aprimorar.despesas.domain.Despesa;
import aprimorar.despesas.dto.DespesaRequest;
import aprimorar.despesas.dto.DespesaResponse;
import aprimorar.despesas.repository.DespesaRepository;
import aprimorar.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DespesaMutationService {

    private static final Logger log = LoggerFactory.getLogger(DespesaMutationService.class);

    private final DespesaRepository despesaRepo;

    public DespesaMutationService(DespesaRepository despesaRepo) {
        this.despesaRepo = despesaRepo;
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
            requested.getCategoria(),
            requested.getValor(),
            requested.getDataPagamento(),
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
    public DespesaResponse togglePagamento(Long despesaId) {
        Despesa despesa = findDespesaOrThrow(despesaId);

        despesa.togglePagamento();
        log.info("Pagamento da despesa {} alternado com sucesso.", despesa.getTitulo().toUpperCase());
        return DespesaResponse.toDto(despesa);
    }

    private Despesa findDespesaOrThrow(Long despesaId) {
        return despesaRepo
            .findById(despesaId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Despesa não encontrada no banco de dados"));
    }
}
