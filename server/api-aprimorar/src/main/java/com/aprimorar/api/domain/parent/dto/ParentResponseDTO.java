package com.aprimorar.api.domain.parent.dto;

import java.time.Instant;
import java.util.UUID;

public record ParentResponseDTO(
        UUID id,
        String name,
        String email,
        String contact,
        String cpf,
        Instant archivedAt,
        Instant createdAt,
        Instant updatedAt
) {
}
