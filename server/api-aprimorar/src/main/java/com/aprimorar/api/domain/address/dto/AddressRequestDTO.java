package com.aprimorar.api.domain.address.dto;

import com.aprimorar.api.enums.BrazilianState;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressRequestDTO(
        @NotBlank(message = "Rua do endereço é obrigatória")
        @Schema(description = "Rua do endereço")
        String street,
        @NotBlank(message = "Número do endereço é obrigatório")
        String number,
        String complement,
        @NotBlank(message = "Bairro do endereço é obrigatório")
        String district,
        @NotBlank(message = "Cidade do endereço é obrigatória")
        String city,
        @NotNull(message = "Estado do endereço é obrigatório")
        BrazilianState state,
        @NotBlank(message = "CEP do endereço é obrigatório")
        String zip
        ) {

}
