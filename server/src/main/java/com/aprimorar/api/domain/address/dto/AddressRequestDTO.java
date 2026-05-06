package com.aprimorar.api.domain.address.dto;

import com.aprimorar.api.enums.BrazilianStates;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressRequestDTO(
        @NotBlank(message = "A rua do endereço é obrigatória")
        @Schema(description = "Rua ou Condomínio", nullable = false, minLength = 3, maxLength = 255)
        String street,

        @NotBlank(message = "O bairro do endereço é obrigatório")
        @Schema(description = "Bairro do endereço", nullable = false, minLength = 3, maxLength = 255)
        String district,

        @NotBlank(message = "A cidade do endereço é obrigatória")
        @Schema(description = "Cidade do endereço", nullable = false, minLength = 3, maxLength = 255)
        String city,

        @NotNull(message = "Estado do endereço é obrigatório")
        @Schema(description = "Estado do endereço")
        BrazilianStates state,

        @NotBlank(message = "O CEP do endereço é obrigatório")
        @Schema(description = "CEP do endereço", nullable = false, minLength = 8, maxLength = 8)
        String zip,

        @Schema(description = "Complemento do endereço", maxLength = 255)
        String complement
) {

}
