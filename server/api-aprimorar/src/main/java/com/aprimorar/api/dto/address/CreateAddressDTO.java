package com.aprimorar.api.dto.address;

import jakarta.validation.constraints.NotBlank;

public record CreateAddressDTO(

        @NotBlank(message = "Rua do endereço é obrigatória")
        String street,

        @NotBlank(message = "Número do endereço é obrigatório")
        String number,

        String complement,

        @NotBlank(message = "Bairro do endereço é obrigatório")
        String district,

        @NotBlank(message = "Cidade do endereço é obrigatória")
        String city,

        @NotBlank(message = "Estado do endereço é obrigatório")
        String state,

        @NotBlank(message = "CEP do endereço é obrigatório")
        String zip
) {
}
