package com.aprimorar.api.domain.address.dto;

import com.aprimorar.api.enums.BrazilianState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

public record AddressResponseDTO(
        String street,
        String number,
        @Schema(nullable = true)
        @Nullable
        String complement,
        String district,
        String city,
        BrazilianState state,
        String zip
) {}
