package com.aprimorar.api.domain.dashboard.api.dto;

import java.math.BigDecimal;

public record ClassesByContentDTO(
    String content,
    long count,
    BigDecimal percentage
) {
}
