package aprimorar.financeiro.api;

import java.time.LocalDate;
import java.util.UUID;

import aprimorar.financeiro.api.dto.DespesaResponseDTO;
import aprimorar.financeiro.api.dto.DespesasResponseDTO;
import aprimorar.financeiro.internal.CategoriaDespesaEnum;
import org.springframework.data.domain.Pageable;

public interface FinanceiroQueryApi {

    DespesasResponseDTO getDespesas(Pageable pageable, CategoriaDespesaEnum categoria, LocalDate startDate, LocalDate endDate);
    DespesaResponseDTO findDespesaById(UUID id);
}
