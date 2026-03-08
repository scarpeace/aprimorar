package com.aprimorar.api.dto.employee;

import com.aprimorar.api.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UpdateEmployeeDTO(
        String name,

        @Past(message = "A data de nascimento do funcionário deve estar no passado")
        LocalDate birthdate,

        String pix,

        @Pattern(regexp = "^\\(\\d{2}\\)\\s?\\d{4,5}-\\d{4}$", message = "Contato deve estar no formato (XX)XXXX-XXXX ou (XX)XXXXX-XXXX")
        String contact,

        @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "CPF deve estar no formato XXX.XXX.XXX-XX")
        String cpf,

        @Email(message = "Email deve ser um endereço de email válido")
        String email,

        Role role
) {
}
