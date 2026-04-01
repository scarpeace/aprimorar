package com.aprimorar.api.domain.employee.dto;

import java.time.LocalDate;

import com.aprimorar.api.enums.Duty;
import com.aprimorar.api.shared.MapperUtils;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public record EmployeeUpdateDTO(
        @NotBlank(message = "Nome do funcionário é obrigatório")
        String name,
        @NotNull(message = "A data de nascimento do funcionário é obrigatória")
        @Past(message = "A data de nascimento do funcionário deve estar no passado")
        LocalDate birthdate,
        @NotBlank(message = "Chave PIX do funcionário é obrigatória")
        String pix,
        @NotBlank(message = "Contato do funcionário é obrigatório")
        @Pattern(regexp = MapperUtils.PHONE_BR, message = MapperUtils.PHONE_BR_MESSAGE)
        String contact,
        @NotBlank(message = "Email do funcionário é obrigatório")
        @Email(message = "Email deve ser um endereço de email válido")
        String email,
        @NotNull(message = "Papel do funcionário é obrigatório")
        Duty duty) {
}
