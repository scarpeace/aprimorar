package com.aprimorar.api.domain.parent.dto;

import java.time.Instant;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

public record ParentResponseDTO(
        UUID id,
        String name,
        String email,
        String contact,
        String cpf,
        @Schema(nullable = true)
        @Nullable
        Instant archivedAt,
        Instant createdAt,
        @Schema(nullable = true)
        @Nullable
        Instant updatedAt
) {}
