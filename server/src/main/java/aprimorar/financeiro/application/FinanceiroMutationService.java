package aprimorar.financeiro.application;

import aprimorar.financeiro.web.dto.DespesaRequestDTO;
import aprimorar.financeiro.web.dto.DespesaResponseDTO;
import aprimorar.financeiro.domain.Despesa;
import aprimorar.financeiro.infrastructure.persistence.DespesaRepository;
import aprimorar.shared.exception.BusinessException;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinanceiroMutationService {

    private static final Logger log = LoggerFactory.getLogger(FinanceiroMutationService.class);

    private final DespesaRepository despesaRepository;
    private final Clock clock;

    public FinanceiroMutationService(DespesaRepository despesaRepository, Clock clock) {
        this.despesaRepository = despesaRepository;
        this.clock = clock;
    }

    @Transactional
    public DespesaResponseDTO createDespesa(DespesaRequestDTO dto) {
        Despesa despesa = new Despesa(dto.amount(), dto.date(), dto.category(), dto.description());
        Despesa savedDespesa = despesaRepository.save(despesa);
        log.info("Despesa {} criada com sucesso.", savedDespesa.getId());
        return DespesaMapper.toDto(savedDespesa);
    }

    @Transactional
    public DespesaResponseDTO updateDespesa(UUID id, DespesaRequestDTO dto) {
        Despesa despesa = findDespesaOrThrow(id);
        despesa.update(dto.amount(), dto.date(), dto.category(), dto.description());
        Despesa updatedDespesa = despesaRepository.save(despesa);
        log.info("Despesa {} atualizada com sucesso.", updatedDespesa.getId());
        return DespesaMapper.toDto(updatedDespesa);
    }

    @Transactional
    public DespesaResponseDTO togglePagamento(UUID id) {
        Despesa despesa = findDespesaOrThrow(id);
        despesa.togglePayment(Instant.now(clock));
        log.info("Status de pagamento da despesa {} atualizado.", despesa.getId());
        return DespesaMapper.toDto(despesa);
    }

    @Transactional
    public void deleteDespesa(UUID id) {
        Despesa despesa = findDespesaOrThrow(id);
        despesaRepository.delete(despesa);
        log.info("Despesa {} removida com sucesso.", despesa.getId());
    }

    private Despesa findDespesaOrThrow(UUID id) {
        return despesaRepository.findById(id).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Despesa não encontrada"));
    }
}
