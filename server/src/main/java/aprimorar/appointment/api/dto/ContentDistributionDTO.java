package aprimorar.appointment.api.dto;

import java.math.BigDecimal;

public record ContentDistributionDTO(
    String content,
    long count,
    BigDecimal percentage
) {
}
