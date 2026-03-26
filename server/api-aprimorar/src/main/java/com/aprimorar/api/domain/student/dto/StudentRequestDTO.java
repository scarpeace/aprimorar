package com.aprimorar.api.domain.student.dto;

import com.aprimorar.api.domain.address.dto.AddressRequestDTO;
import com.aprimorar.api.domain.parent.dto.ParentRequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record StudentRequestDTO(
    @NotBlank()
    @Schema(description = "Nome do aluno")
    String name,

    @NotNull()
    @PastOrPresent()
    @Schema(description = "Data de nascimento do aluno", nullable = false)
    LocalDate birthdate,

    @NotNull()
    @Schema(description = "CPF do aluno", nullable = false)
    String cpf,

    @NotBlank()
    @Schema(description = "Escola do aluno", nullable = false)
    String school,

    @NotBlank()
    @Schema(description = "Contato do aluno", nullable = false)
    String contact,

    @NotBlank()
    @Email()
    @Schema(description = "Email do aluno", nullable = false)
    String email,

    @NotNull()
    @Schema(description = "Endereço do aluno", implementation = AddressRequestDTO.class)
    @Valid
    AddressRequestDTO address,

    @NotNull()
    @Schema(description = "Responsável do aluno", implementation = ParentRequestDTO.class)
    @Valid
    ParentRequestDTO parent
) {}
