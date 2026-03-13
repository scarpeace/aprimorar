package com.aprimorar.api.domain.address.dto;

import com.aprimorar.api.enums.BrazilianState;

public record AddressResponseDTO(
        String street,
        String number,
        String complement,
        String district,
        String city,
        BrazilianState state,
        String zip
) {
}
