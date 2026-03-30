package com.aprimorar.api.domain.address.dto;

import com.aprimorar.api.enums.BrazilianState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AddressResponseDTO(
        @NotNull String street,
        @NotNull String number,
        @NotNull String district,
        @NotNull String city,
        @NotNull BrazilianState state,
        @NotNull String zip,
        @Schema(nullable = true)
        String complement

) {
}
