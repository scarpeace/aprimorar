package com.aprimorar.api.domain.student.dto;

import com.aprimorar.api.domain.address.dto.AddressRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

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
    @Schema(implementation = AddressRequestDTO.class)
    @Valid
    AddressRequestDTO address,

    @NotNull()
    @Schema(description = "ID do responsável do aluno", nullable = false)
    UUID parentId
) {}
