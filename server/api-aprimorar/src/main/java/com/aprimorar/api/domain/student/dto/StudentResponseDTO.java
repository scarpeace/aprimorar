package com.aprimorar.api.domain.student.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import com.aprimorar.api.domain.address.dto.AddressResponseDTO;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

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
        AddressResponseDTO address,
        ParentResponseDTO parent,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
        Instant createdAt,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
        Instant archivedAt
) {
}
