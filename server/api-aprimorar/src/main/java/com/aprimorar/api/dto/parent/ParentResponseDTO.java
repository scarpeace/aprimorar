package com.aprimorar.api.dto.parent;

import java.util.UUID;

public record ParentResponseDTO(
        UUID id,
        String name,
        String email,
        String contact,
        String cpf) {
}
