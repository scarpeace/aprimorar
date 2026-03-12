package com.aprimorar.api.domain.parent.dto;

import java.util.UUID;

public record ParentResponseDTO(
        UUID id,
        String name,
        String email,
        String contact,
        String cpf) {
}
