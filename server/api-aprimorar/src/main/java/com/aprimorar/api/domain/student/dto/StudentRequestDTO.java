package com.aprimorar.api.domain.student.dto;

import java.time.LocalDate;

import com.aprimorar.api.domain.address.Address;
import com.aprimorar.api.domain.parent.Parent;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record StudentRequestDTO(
                @NotBlank(message = "Nome do estudante é obrigatório") String name,
                @NotNull(message = "A data de nascimento do estudante é obrigatória") @PastOrPresent(message = "A data de nascimento do estudante não pode ser no futuro") LocalDate birthdate,
                @NotNull(message = "CPF do estudante é obrigatório") String cpf,
                @NotBlank(message = "Escola do estudante é obrigatória") String school,
                @NotBlank(message = "Contato do estudante é obrigatório") String contact,
                @NotBlank(message = "Email do estudante é obrigatório") @Email(message = "Email deve ser um endereço de email válido") String email,
                @NotNull(message = "Endereço do estudante é obrigatório") @Valid Address address,
                @NotNull(message = "Responsável do estudante é obrigatório") Parent parent) {
}
