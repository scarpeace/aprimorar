package com.aprimorar.api.domain.parent.dto;

import java.time.Instant;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record ParentResponseDTO(
        @NotNull UUID id,
        @NotNull String name,
        @NotNull String email,
        @NotNull String contact,
        @NotNull String cpf,
        @Schema(nullable = true)
        @Nullable
        Instant archivedAt,
        @NotNull Instant createdAt,
        @Schema(nullable = true)
        @Nullable
        Instant updatedAt
) {}
