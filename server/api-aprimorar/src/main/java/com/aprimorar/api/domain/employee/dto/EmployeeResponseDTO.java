package com.aprimorar.api.domain.employee.dto;

import com.aprimorar.api.enums.Duty;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record EmployeeResponseDTO(
        @NotNull UUID id,
        @NotNull String name,
        @NotNull LocalDate birthdate,
        @NotNull String pix,
        @NotNull String contact,
        @NotNull String cpf,
        @NotNull String email,
        @NotNull Duty duty,
        @Schema(nullable = true)
        @Nullable
        Instant archivedAt,
        @NotNull Instant createdAt,
        @Schema(nullable = true)
        @Nullable
        Instant updatedAt
) {}
