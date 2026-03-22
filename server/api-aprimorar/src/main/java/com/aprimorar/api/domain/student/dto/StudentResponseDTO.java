package com.aprimorar.api.domain.student.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import com.aprimorar.api.domain.address.dto.AddressResponseDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;

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
    @Nullable
    Instant archivedAt,
    Instant createdAt,
    @Nullable
    Instant updatedAt
) {
}
