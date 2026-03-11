package com.aprimorar.api.domain.student.dto;

import java.time.LocalDate;

import com.aprimorar.api.domain.address.dto.AddressRequestDTO;
import com.aprimorar.api.domain.parent.dto.ParentRequestDTO;
import com.aprimorar.api.shared.MapperUtils;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

public record UpdateStudentDTO(
        
    @NotBlank(message = "O nome do estudante é obrigatório")
        String name,

        @NotBlank(message = "A data de nascimento do estudante é obrigatória")
        @PastOrPresent(message = "A data de nascimento do estudante não pode ser futura")
        LocalDate birthdate,

        @NotBlank(message = "O CPF do estudante é obrigatório")
        @Pattern(regexp = MapperUtils.CPF, message = MapperUtils.CPF_MESSAGE)
        String cpf,

        @NotBlank(message = "A escola do estudante é obrigatória")
        String school,

        @NotBlank(message = "O contato do estudante é obrigatório")
        @Pattern(regexp = MapperUtils.PHONE_BR, message = MapperUtils.PHONE_BR_MESSAGE)
        String contact,

        @NotBlank(message = "O email do estudante é obrigatório")
        @Email(message = "Email deve ser um endereço de email válido")
        String email,
        
        @NotNull(message = "O endereço do estudante é obrigatório")
        @Valid
        AddressRequestDTO address,
        
        @NotNull(message = "As informações do responsável são obrigatórias")
        @Valid
        ParentRequestDTO parent
        ) {
}
