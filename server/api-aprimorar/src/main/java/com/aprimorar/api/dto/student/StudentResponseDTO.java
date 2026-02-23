package com.aprimorar.api.dto.student;

import com.aprimorar.api.dto.address.AddressResponseDTO;
import com.aprimorar.api.dto.parent.ParentResponseDto;
import com.aprimorar.api.enums.Activity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record StudentResponseDto(
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
        ParentResponseDto parent,
        Instant createdAt,
        Instant updatedAt
) {
}