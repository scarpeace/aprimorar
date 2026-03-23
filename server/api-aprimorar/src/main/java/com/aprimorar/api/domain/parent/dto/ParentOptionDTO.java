package com.aprimorar.api.domain.parent.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;

public record ParentOptionDTO(
        @NotNull UUID id,
        @NotNull String name
) {
}
