package com.aprimorar.api.domain.student.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.aprimorar.api.domain.address.dto.AddressRequestDTO;
import com.aprimorar.api.domain.parent.dto.ParentRequestDTO;
import com.aprimorar.api.shared.MapperUtils;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

public record StudentRequestDTO(
        @NotBlank(message = "Nome do estudante é obrigatório")
        String name,
        @NotNull(message = "A data de nascimento do estudante é obrigatória")
        @PastOrPresent(message = "A data de nascimento do estudante não pode ser no futuro")
        LocalDate birthdate,
        @NotNull(message = "CPF do estudante é obrigatório")
        @Pattern(regexp = MapperUtils.CPF, message = MapperUtils.CPF_MESSAGE)
        String cpf,
        @NotBlank(message = "Escola do estudante é obrigatória")
        String school,
        @NotBlank(message = "Contato do estudante é obrigatório")
        @Pattern(regexp = MapperUtils.PHONE_BR, message = MapperUtils.PHONE_BR_MESSAGE)
        String contact,
        @NotBlank(message = "Email do estudante é obrigatório")
        @Email(message = "Email deve ser um endereço de email válido")
        String email,
        @NotNull(message = "Endereço do estudante é obrigatório")
        @Valid
        AddressRequestDTO address,
        @NotNull(message = "Responsável do estudante é obrigatório")
        ParentRequestDTO parentRequestDTO
        ) {
}
