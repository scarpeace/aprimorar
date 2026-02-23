package com.aprimorar.api.dto.student;

import com.aprimorar.api.dto.address.CreateAddressDTO;
import com.aprimorar.api.dto.parent.CreateParentDTO;
import com.aprimorar.api.enums.Activity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

//TODO Adicionar validação da idade mínima do aluno.

public record CreateStudentDTO(
        @NotNull(message = "Student name can't be null")
        String name,

        @NotNull(message = "Student birthdate can't be null")
        @Past(message = "Student birthdate should be in the past")
        LocalDate birthdate,

        @NotNull(message = "Student CPF can't be null")
        @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "CPF must be in format XXX.XXX.XXX-XX")
        String cpf,

        @NotBlank(message = "Student school can't be blank")
        String school,

        @NotBlank(message = "Student contact number can't be blank")
        @Pattern(regexp = "^\\(\\d{2}\\)\\d{4,5}-\\d{4}$", message = "Contact must be in format (XX)XXXXX-XXXX")
        String contact,

        @NotBlank(message = "Student email can't be blank")
        @Email
        String email,

        @NotNull(message = "Student activity can't be null")
        Activity activity,

        @NotNull(message = "Student Address can't be null")
        @Valid
        CreateAddressDTO address,

        @NotNull(message = "Student Parent can't be null")
        @Valid
        CreateParentDTO parent
) {
}
