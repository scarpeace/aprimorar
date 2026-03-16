package com.aprimorar.api.domain.employee.dto;

import com.aprimorar.api.enums.Duty;
import com.aprimorar.api.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record EmployeeResponseDTO(
        UUID id,
        String name,

        LocalDate birthdate,
        String pix,
        String contact,
        String cpf,
        String email,
        Duty duty,
        Instant archivedAt,
        Instant createdAt,
        Instant updatedAt
        ) {
}
