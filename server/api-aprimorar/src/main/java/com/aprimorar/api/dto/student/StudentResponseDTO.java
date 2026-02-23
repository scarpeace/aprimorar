package com.aprimorar.api.dto.student;

import com.aprimorar.api.dto.address.AddressResponseDTO;
import com.aprimorar.api.dto.parent.ParentResponseDTO;
import com.aprimorar.api.enums.Activity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record StudentResponseDTO(
        UUID id,
        String name,
        String contact,
        String email,
        LocalDate birthdate,
        String cpf,
        String school,
        Activity activity,
        Boolean active,
        AddressResponseDTO address,
        ParentResponseDTO parent,
        Instant createdAt,
        Instant updatedAt
) {
}