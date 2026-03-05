package com.aprimorar.api.dto.parent;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateParentDTO(
        @NotNull(message = "Nome do responsável é obrigatório")
        String name,

        @NotNull(message = "Email do responsável é obrigatório")
        @Email(message = "Email deve ser um endereço de email válido")
        String email,

        @NotNull(message = "Contato do responsável é obrigatório")
        @Pattern(regexp = "^\\(\\d{2}\\)\\d{5}-\\d{4}$", message = "Contato deve estar no formato (XX)XXXXX-XXXX")
        String contact,

        @NotNull(message = "CPF do responsável é obrigatório")
        @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "CPF deve estar no formato XXX.XXX.XXX-XX")
        String cpf
) {
}
