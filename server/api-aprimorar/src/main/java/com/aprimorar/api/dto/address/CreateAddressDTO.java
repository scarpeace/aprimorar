package com.aprimorar.api.dto.address;

import jakarta.validation.constraints.NotBlank;

public record CreateAddressDTO(

        @NotBlank(message = "Address street can't be blank")
        String street,

        @NotBlank(message = "Address number can't be blank")
        String number,

        String complement,

        @NotBlank(message = "Address district can't be blank")
        String district,

        @NotBlank(message = "Address city can't be blank")
        String city,

        @NotBlank(message = "Address state can't be blank")
        String state,

        @NotBlank(message = "Address zip code can't be blank")
        String zip
) {
}
