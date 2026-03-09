package com.aprimorar.api.dto.employee;

import com.aprimorar.api.enums.Role;
import com.aprimorar.api.validation.ValidationPatterns;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record CreateEmployeeDTO (

        @NotBlank(message = "Nome do funcionário é obrigatório")
        String name,

        @NotNull(message = "A data de nascimento do funcionário é obrigatória")
        @Past(message = "A data de nascimento do funcionário deve estar no passado")
        LocalDate birthdate,

        @NotBlank(message = "Chave PIX do funcionário é obrigatória")
        String pix,

        @NotBlank(message = "Contato do funcionário é obrigatório")
        @Pattern(regexp = ValidationPatterns.PHONE_BR, message = ValidationPatterns.PHONE_BR_MESSAGE)
        String contact,

        @NotBlank(message = "CPF do funcionário é obrigatório")
        @Pattern(regexp = ValidationPatterns.CPF, message = ValidationPatterns.CPF_MESSAGE)
        String cpf,

        @NotBlank(message = "Email do funcionário é obrigatório")
        @Email(message = "Email deve ser um endereço de email válido")
        String email,

        @NotNull(message = "Papel do funcionário é obrigatório")
        Role role) {
}
