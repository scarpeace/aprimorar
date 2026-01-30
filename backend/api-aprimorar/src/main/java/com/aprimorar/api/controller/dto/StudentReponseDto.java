package com.aprimorar.api.controller.dto;

import com.aprimorar.api.enums.Activity;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public record StudentReponseDto(UUID id,
                                String name,
                                String cpf,
                                String school,
                                String phone,
                                Date birthdate,
                                Activity activity,
                                AddressResponseDto address,
                                ParentResponseDto parent,
                                Instant createdAt) {
}
