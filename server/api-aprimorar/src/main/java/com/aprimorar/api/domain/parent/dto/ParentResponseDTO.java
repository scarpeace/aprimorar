package com.aprimorar.api.domain.parent.dto;

import java.time.Instant;
import java.util.UUID;

import jakarta.annotation.Nullable;

public record ParentResponseDTO(
        UUID id,
        String name,
        String email,
        String contact,
        String cpf,
        @Nullable
        Instant archivedAt,
        Instant createdAt,
        @Nullable
        Instant updatedAt
) {
}
