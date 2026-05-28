package aprimorar.financeiro.api;

import java.time.Instant;
import java.util.UUID;

import aprimorar.financeiro.api.dto.DespesaResponseDTO;
import aprimorar.financeiro.api.dto.DespesasResponseDTO;

import org.springframework.data.domain.Pageable;

public interface FinanceiroQueryApi {

    DespesasResponseDTO getDespesas(Pageable pageable, CategoriaDespesaEnum categoria, Instant startDate, Instant endDate);
    DespesaResponseDTO findDespesaById(UUID id);
}
