package com.aprimorar.api.dto.common;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PageQuery(
    @Min(value = 0, message = "Parâmetro 'page' deve ser >= 0")
    int page,

    @Min(value = 1, message = "Parâmetro 'size' deve estar entre 1 e 100")
    @Max(value = 100, message = "Parâmetro 'size' deve estar entre 1 e 100")
    int size

) {}
