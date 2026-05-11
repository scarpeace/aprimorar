package aprimorar.event.api.dto;

import java.math.BigDecimal;

public record ContentDistributionDTO(
    String content,
    long count,
    BigDecimal percentage
) {
}
