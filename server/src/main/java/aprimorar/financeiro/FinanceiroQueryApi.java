package aprimorar.financeiro;

import java.time.Instant;
import java.util.UUID;

import aprimorar.financeiro.web.dto.DespesaResponseDTO;
import aprimorar.financeiro.web.dto.DespesasResponseDTO;

import aprimorar.financeiro.domain.CategoriaDespesaEnum;
import org.springframework.data.domain.Pageable;

public interface FinanceiroQueryApi {

    DespesasResponseDTO getDespesas(Pageable pageable, CategoriaDespesaEnum categoria, Instant startDate, Instant endDate);
    DespesaResponseDTO findDespesaById(UUID id);
}
