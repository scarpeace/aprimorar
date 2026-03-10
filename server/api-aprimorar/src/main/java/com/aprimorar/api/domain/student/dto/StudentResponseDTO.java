package com.aprimorar.api.domain.student.dto;

import com.aprimorar.api.domain.address.dto.AddressResponseDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record StudentResponseDTO(
        UUID id,
        String name,
        String contact,
        String email,
        String cpf,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate birthdate,

        String school,
        Integer age,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
        Instant archivedAt,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
        Instant lastReactivatedAt,
        AddressResponseDTO address,
        ParentResponseDTO parent,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
        Instant createdAt
) {
}
