package aprimorar.financeiro.api.dto;

import aprimorar.financeiro.api.CategoriaDespesa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DespesaRequestDTO(
    @NotNull @Positive BigDecimal amount,
    @NotNull LocalDate date,
    @NotNull CategoriaDespesa category,
    @NotBlank String description
) {}
