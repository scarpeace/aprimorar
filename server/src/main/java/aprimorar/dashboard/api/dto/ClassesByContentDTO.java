package aprimorar.dashboard.api.dto;

import java.math.BigDecimal;

public record ClassesByContentDTO(
    String content,
    long count,
    BigDecimal percentage
) {
}
