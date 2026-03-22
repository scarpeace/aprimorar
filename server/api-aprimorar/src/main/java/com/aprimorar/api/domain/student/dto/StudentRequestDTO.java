package com.aprimorar.api.domain.student.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.aprimorar.api.domain.address.dto.AddressRequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record StudentRequestDTO(
    @NotBlank(message = "Nome do aluno é obrigatório")
    @Schema(description = "Nome do aluno")
    String name,

    @Schema(description = "Data de nascimento do aluno")
    @NotNull(message = "A data de nascimento do aluno é obrigatória")
    @PastOrPresent(message = "A data de nascimento do aluno não pode ser no futuro")
    LocalDate birthdate,

    @Schema(description = "CPF do aluno") 
    @NotNull(message = "CPF do aluno é obrigatório")
    String cpf,

    @Schema(description = "Escola do aluno") 
    @NotBlank(message = "Escola do aluno é obrigatória")
    String school,

    @Schema(description = "Telefone de contato do aluno") 
    @NotBlank(message = "Contato do aluno é obrigatório")
    String contact,

    @Schema(description = "Email do aluno") 
    @NotBlank(message = "Email do aluno é obrigatório")
    @Email(message = "Email deve ser um endereço de email válido")
    String email,

    @Schema(description = "Endereço do aluno como AdressRequestDTO") 
    @NotNull(message = "Endereço do aluno é obrigatório")
    @Valid AddressRequestDTO address,

    @Schema(description = "UUID do responsável") 
    @NotNull(message = "Responsável do aluno é obrigatório")
    UUID parentId) {
}
