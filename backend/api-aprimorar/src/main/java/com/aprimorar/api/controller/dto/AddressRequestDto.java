package com.aprimorar.api.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressRequestDto(

        @NotNull(message = "Address street can't be null")
        @NotBlank
        String street,

        @NotNull(message = "Address district can't be null")
        @NotBlank
        String district,

        @NotBlank(message = "Address city can't be blank")
        @NotBlank
        String city,

        @NotNull(message = "Address state can't be null")
        @NotBlank
        String state,

        @NotNull(message = "Adress zip code can't be null")
        @NotBlank
        String zipCode
) {
}
