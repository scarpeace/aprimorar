package com.aprimorar.api.domain.address.dto;

import com.aprimorar.api.shared.MapperUtils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateAddressDTO(
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
        @Pattern(regexp = MapperUtils.ZIP_CODE_BR, message = MapperUtils.ZIP_CODE_BR_MESSAGE)
        String zip
        ) {}
