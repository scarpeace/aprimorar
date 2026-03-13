package com.aprimorar.api.domain.student.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import com.aprimorar.api.domain.address.Address;
import com.aprimorar.api.domain.parent.Parent;
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
        Address address,
        Parent parent,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        Instant archivedAt,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        Instant createdAt,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
        Instant updatedAt
) {
}
