package com.aprimorar.api.domain.employee.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;

public record EmployeeOptionDTO(
        @NotNull UUID id,
        @NotNull String name
) {
}
