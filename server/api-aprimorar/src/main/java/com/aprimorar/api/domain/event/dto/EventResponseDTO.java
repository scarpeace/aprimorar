package com.aprimorar.api.domain.event.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record EventResponseDTO(
        @NotNull UUID id,
        @NotNull String title,
        @Schema(nullable = true)
        @Nullable
        String description,
        @NotNull String content,
        @NotNull Instant startDate,
        @NotNull Instant endDate,
        @NotNull BigDecimal price,
        @NotNull BigDecimal payment,
        @NotNull UUID studentId,
        @NotNull String studentName,
        @NotNull UUID employeeId,
        @NotNull String employeeName,
        @NotNull Instant createdAt,
        @Schema(nullable = true)
        @Nullable
        Instant updatedAt
) {}
