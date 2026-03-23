package com.aprimorar.api.domain.student.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import com.aprimorar.api.domain.address.dto.AddressResponseDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

public record StudentResponseDTO(
        UUID id,
        String name,
        String contact,
        String email,
        String cpf,
        LocalDate birthdate,
        String school,
        Integer age,
        AddressResponseDTO address,
        ParentResponseDTO parent,
        @Schema(nullable = true)
        @Nullable
        Instant archivedAt,
        Instant createdAt,
        @Schema(nullable = true)
        @Nullable
        Instant updatedAt
) {}
