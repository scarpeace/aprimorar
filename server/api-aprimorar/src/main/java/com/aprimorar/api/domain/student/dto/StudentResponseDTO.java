package com.aprimorar.api.domain.student.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import com.aprimorar.api.domain.address.Address;
import com.aprimorar.api.domain.parent.Parent;

import io.swagger.v3.oas.annotations.media.Schema;

public record StudentResponseDTO(
        UUID id,
        String name,
        String contact,
        String email,
        String cpf,
        LocalDate birthdate,
        String school,
        Integer age,
        Address address,
        Parent parent,
        Instant archivedAt,
        Instant createdAt,
        Instant updatedAt
) {
}
