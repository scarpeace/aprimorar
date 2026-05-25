package aprimorar.financeiro.internal;

import aprimorar.financeiro.api.dto.DespesaRequestDTO;
import aprimorar.financeiro.api.dto.DespesaResponseDTO;
import aprimorar.financeiro.internal.repository.DespesaRepository;
import aprimorar.shared.exception.BusinessException;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinanceiroMutationService {

    private final DespesaRepository despesaRepository;
    private final Clock clock;

    public FinanceiroMutationService(DespesaRepository despesaRepository, Clock clock) {
        this.despesaRepository = despesaRepository;
        this.clock = clock;
    }

    @Transactional
    public DespesaResponseDTO createDespesa(DespesaRequestDTO dto) {
        Despesa despesa = new Despesa(dto.amount(), dto.date(), dto.category(), dto.description());
        return DespesaMapper.toDto(despesaRepository.save(despesa));
    }

    @Transactional
    public DespesaResponseDTO updateDespesa(UUID id, DespesaRequestDTO dto) {
        Despesa despesa = findDespesaOrThrow(id);
        despesa.update(dto.amount(), dto.date(), dto.category(), dto.description());
        return DespesaMapper.toDto(despesaRepository.save(despesa));
    }

    @Transactional
    public DespesaResponseDTO togglePagamento(UUID id) {
        Despesa despesa = findDespesaOrThrow(id);
        despesa.togglePayment(Instant.now(clock));
        return DespesaMapper.toDto(despesa);
    }

    @Transactional
    public void deleteDespesa(UUID id) {
        despesaRepository.delete(findDespesaOrThrow(id));
    }

    private Despesa findDespesaOrThrow(UUID id) {
        return despesaRepository.findById(id).orElseThrow(()-> new BusinessException(HttpStatus.NOT_FOUND, "Despesa não encontrada"));
    }
}
