package com.aprimorar.api.domain.address.dto;

import com.aprimorar.api.enums.BrazilianState;

import jakarta.annotation.Nullable;

public record AddressResponseDTO(
        String street,
        String number,
        @Nullable
        String complement,
        String district,
        String city,
        BrazilianState state,
        String zip
) {
}
