package aprimorar.atendimentos.dto;

import java.math.BigDecimal;

import aprimorar.atendimentos.enums.CategoriaTransacao;

public record TransacaoRequestDTO(
    BigDecimal valor,
    CategoriaTransacao categoria
) {
}
