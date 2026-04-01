package com.aprimorar.api.domain.student.dto;

import com.aprimorar.api.domain.address.dto.AddressRequestDTO;
import com.aprimorar.api.domain.parent.dto.ParentUpdateDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Schema(description = "Formato de payload para o cadastro e/ou update de um aluno")
public record StudentUpdateDTO(
    @NotBlank(message = "Nome do aluno é obrigatório")
    @Schema(nullable = false, description = "Nome do aluno", example = "John Doe")
    String name,

    @NotNull(message = "Data de nascimento é obrigatória")
    @PastOrPresent()
    @Schema(nullable = false, description = "Data de nascimento do aluno", example = "2000-01-01")
    LocalDate birthdate,

    @NotBlank(message = "Escola do aluno é obrigatória")
    @Schema(nullable = false,description = "Escola do aluno", example = "School Name")
    String school,

    @NotBlank(message = "Contato do aluno é obrigatório")
    @Schema(nullable = false,description = "Contato do aluno", example = "(61) 99999-9999")
    String contact,

    @NotBlank(message = "Email do aluno é obrigatório")
    @Email()
    @Schema(nullable = false,description = "Email do aluno", example = "john.doe@example.com")
    String email,

    @NotNull(message = "Endereço do aluno é obrigatório")
    @Schema(nullable = false,description = "Endereço do aluno", implementation = AddressRequestDTO.class)
    @Valid
    AddressRequestDTO address,

    @NotNull(message = "Responsável do aluno é obrigatório")
    @Schema(nullable = false,description = "Responsável do aluno", implementation = ParentUpdateDTO.class)
    @Valid
    ParentUpdateDTO parent
) {}
