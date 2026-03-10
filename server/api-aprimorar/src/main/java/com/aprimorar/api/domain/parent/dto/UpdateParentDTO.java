package com.aprimorar.api.domain.parent.dto;

import com.aprimorar.api.shared.MapperUtils;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdateParentDTO(
        @NotNull(message = "Nome do responsável é obrigatório")
        String name,

        @NotNull(message = "Email do responsável é obrigatório")
        @Email(message = "Email deve ser um endereço de email válido")
        String email,

        @NotNull(message = "Contato do responsável é obrigatório")
        @Pattern(regexp = MapperUtils.PHONE_BR, message = MapperUtils.PHONE_BR_MESSAGE)
        String contact,

        @NotNull(message = "CPF do responsável é obrigatório")
        @Pattern(regexp = MapperUtils.CPF, message = MapperUtils.CPF_MESSAGE)
        String cpf
) {
}
