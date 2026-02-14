package com.aprimorar.api.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressRequestDto(

        @NotNull(message = "Address street can't be null")
        @NotBlank(message = "Address street can't be blank")
        String street,

        @NotNull(message = "Address district can't be null")
        @NotBlank(message = "Address district can't be blank")
        String district,

        @NotNull(message = "Address city can't be null")
        @NotBlank(message = "Address city can't be blank")
        String city,

        @NotNull(message = "Address state can't be null")
        @NotBlank(message = "Address state can't be blank")
        String state,

        @NotNull(message = "Address zip code can't be null")
        @NotBlank(message = "Address zip code can't be blank")
        String zipCode
) {
}
