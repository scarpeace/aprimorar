package com.aprimorar.api.domain.employee.dto;

import com.aprimorar.api.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record EmployeeResponseDTO(
        UUID id,
        String name,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate birthdate,
        String pix,
        String contact,
        String cpf,
        String email,
        Role role,
        Boolean archived,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
        Instant createdAt,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
        Instant updatedAt
) {
}
