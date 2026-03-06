package com.aprimorar.api.dto.employee;

import com.aprimorar.api.enums.Role;
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
        @Pattern(regexp = "^\\(\\d{2}\\)\\s?\\d{4,5}-\\d{4}$", message = "Contato deve estar no formato (XX)XXXX-XXXX ou (XX)XXXXX-XXXX")
        String contact,

        @NotBlank(message = "CPF do funcionário é obrigatório")
        @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "CPF deve estar no formato XXX.XXX.XXX-XX")
        String cpf,

        @NotBlank(message = "Email do funcionário é obrigatório")
        @Email(message = "Email deve ser um endereço de email válido")
        String email,

        @NotNull(message = "Papel do funcionário é obrigatório")
        Role role) {
}
