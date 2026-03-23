package com.aprimorar.api.domain.student.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import com.aprimorar.api.domain.address.dto.AddressResponseDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record StudentResponseDTO(
        @NotNull UUID id,
        @NotNull String name,
        @NotNull String contact,
        @NotNull String email,
        @NotNull String cpf,
        @NotNull LocalDate birthdate,
        @NotNull String school,
        @NotNull Integer age,
        @NotNull AddressResponseDTO address,
        @NotNull ParentResponseDTO parent,
        @Schema(nullable = true)
        @Nullable
        Instant archivedAt,
        @NotNull Instant createdAt,
        @Schema(nullable = true)
        @Nullable
        Instant updatedAt
) {}
