package com.aprimorar.api.domain.employee.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

public record UpdateEmployeeDTO(
        @NotBlank(message = "Nome do funcionário é obrigatório")
        String name,
        @NotNull(message = "A data de nascimento do funcionário é obrigatória")
        LocalDate birthdate,
        @NotBlank(message = "Chave PIX do funcionário é obrigatória")
        String pix,
        @NotBlank(message = "Contato do funcionário é obrigatório")
        String contact,
        @NotBlank(message = "CPF do funcionário é obrigatório")
        String cpf,
        @NotBlank(message = "Email do funcionário é obrigatório")
        @Email(message = "Email deve ser um endereço de email válido")
        String email
) {
}
