package com.aprimorar.api.dto.student;

import com.aprimorar.api.dto.address.AddressResponseDTO;
import com.aprimorar.api.dto.parent.ParentResponseDTO;
import com.aprimorar.api.enums.Activity;
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
        Activity activity,
        Boolean active,
        AddressResponseDTO address,
        ParentResponseDTO parent,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
        Instant createdAt
) {
}