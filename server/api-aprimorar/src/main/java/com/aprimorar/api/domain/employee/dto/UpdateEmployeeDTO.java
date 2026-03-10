package com.aprimorar.api.domain.employee.dto;

import java.time.LocalDate;

import com.aprimorar.api.enums.Role;
import com.aprimorar.api.shared.MapperUtils;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public record UpdateEmployeeDTO(
        String name,
        @Past(message = "A data de nascimento do funcionário deve estar no passado")
        LocalDate birthdate,
        String pix,
        @Pattern(regexp = MapperUtils.PHONE_BR, message = MapperUtils.PHONE_BR_MESSAGE)
        String contact,
        @Pattern(regexp = MapperUtils.CPF, message = MapperUtils.CPF_MESSAGE)
        String cpf,
        @Email(message = "Email deve ser um endereço de email válido")
        String email,
        Role role
        ) {

}
