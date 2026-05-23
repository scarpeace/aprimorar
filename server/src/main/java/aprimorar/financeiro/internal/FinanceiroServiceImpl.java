package aprimorar.financeiro.internal;

import aprimorar.financeiro.api.CategoriaDespesa;
import aprimorar.financeiro.api.FinanceiroService;
import aprimorar.financeiro.api.dto.DespesaRequestDTO;
import aprimorar.financeiro.api.dto.DespesaResponseDTO;
import aprimorar.financeiro.api.dto.ResumoDespesasDTO;
import aprimorar.financeiro.api.exception.DespesaNotFoundException;
import aprimorar.shared.PageDTO;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinanceiroServiceImpl implements FinanceiroService, FinanceiroManagementService {

    private final DespesaRepository despesaRepository;
    private final Clock clock;

    public FinanceiroServiceImpl(DespesaRepository despesaRepository, Clock clock) {
        this.despesaRepository = despesaRepository;
        this.clock = clock;
    }

    @Transactional
    public DespesaResponseDTO createExpense(DespesaRequestDTO dto) {
        Despesa despesa = new Despesa(dto.amount(), dto.date(), dto.category(), dto.description());
        return toDto(despesaRepository.save(despesa));
    }

    @Transactional(readOnly = true)
    public ResumoDespesasDTO getExpenses(CategoriaDespesa category, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<DespesaResponseDTO> page = despesaRepository.findFiltered(category, startDate, endDate, pageable).map(this::toDto);
        return new ResumoDespesasDTO(
            new PageDTO<>(page),
            despesaRepository.sumFiltered(startDate, endDate),
            despesaRepository.sumPendingFiltered(startDate, endDate)
        );
    }

    @Transactional(readOnly = true)
    public DespesaResponseDTO findExpenseById(UUID id) {
        return toDto(findExpenseOrThrow(id));
    }

    @Transactional
    public DespesaResponseDTO updateExpense(UUID id, DespesaRequestDTO dto) {
        Despesa despesa = findExpenseOrThrow(id);
        despesa.update(dto.amount(), dto.date(), dto.category(), dto.description());
        return toDto(despesaRepository.save(despesa));
    }

    @Transactional
    public DespesaResponseDTO togglePayment(UUID id) {
        Despesa despesa = findExpenseOrThrow(id);
        despesa.togglePayment(Instant.now(clock));
        return toDto(despesa);
    }

    @Transactional
    public void deleteExpense(UUID id) {
        despesaRepository.delete(findExpenseOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal sumExpenses(Instant startDate, Instant endDate) {
        return despesaRepository.sumFiltered(toUtcLocalDate(startDate), toUtcLocalDate(endDate));
    }

    @Transactional(readOnly = true)
    public BigDecimal sumPendingExpenses(Instant startDate, Instant endDate) {
        return despesaRepository.sumPendingFiltered(toUtcLocalDate(startDate), toUtcLocalDate(endDate));
    }

    private Despesa findExpenseOrThrow(UUID id) {
        return despesaRepository.findById(id).orElseThrow(DespesaNotFoundException::new);
    }

    private DespesaResponseDTO toDto(Despesa expense) {
        return new DespesaResponseDTO(
            expense.getId(),
            expense.getAmount(),
            expense.getDate(),
            expense.getCategory(),
            expense.getDescription(),
            expense.getPaymentDate()
        );
    }

    private LocalDate toUtcLocalDate(Instant instant) {
        return instant == null ? null : instant.atZone(ZoneOffset.UTC).toLocalDate();
    }
}
