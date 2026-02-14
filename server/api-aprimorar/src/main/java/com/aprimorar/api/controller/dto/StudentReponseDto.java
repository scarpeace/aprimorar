package com.aprimorar.api.controller.dto;

import com.aprimorar.api.enums.Activity;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public record StudentReponseDto(
        UUID id,
        String name,
        String contact,
        String email,
        Date birthdate,
        String cpf,
        String school,
        Activity activity,
        Boolean active,
        AddressResponseDto address,
        ParentResponseDto parent,
        Instant createdAt,
        Instant updatedAt
) {
}