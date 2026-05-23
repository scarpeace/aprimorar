package aprimorar.atendimentos.api.dto;

import java.math.BigDecimal;

public record ContentDistributionDTO(
    String content,
    long count,
    BigDecimal percentage
) {
}
