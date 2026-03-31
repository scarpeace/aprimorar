package com.aprimorar.api.domain.student.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;

public record StudentOptionsDTO(
        @NotNull UUID id,
        @NotNull String name
) {
}
