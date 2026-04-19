package com.aprimorar.api.domain.student.dto;

import com.aprimorar.api.domain.address.dto.AddressResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Dados do aluno retornados pela API")
public record StudentResponseDTO(
    @NotNull
    @Schema(description = "Indentificador único do aluno", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,

    @NotNull
    @Schema(example = "John Doe", description = "Nome do aluno")
    String name,

    @NotNull
    @Schema(example = "123456789", description = "Contato do aluno")
    String contact,

    @NotNull
    @Schema(example = "john.doe@example.com", description = "Email do aluno")
    String email,

    @NotNull
    @Schema(example = "123.456.789-00", description = "CPF do aluno")
    String cpf,

    @NotNull
    @Schema(example = "1990-01-01", description = "Data de nascimento do aluno")
    LocalDate birthdate,

    @NotNull
    @Schema(example = "School Name", description = "Nome da escola do aluno")
    String school,

    @NotNull
    @Schema(example = "30", description = "Idade do aluno")
    Integer age,

    @NotNull
    @Schema(implementation = AddressResponseDTO.class, example = "Street Name", description = "Endereço do aluno")
    AddressResponseDTO address,

    @NotNull
    @Schema(example = "eb4be7ae-ebc6-423a-a380-da97f4a81511", description = "ID do responsável")
    UUID parentId,

    @NotNull
    @Schema(description = "Resumo do responsável vinculado ao aluno")
    StudentResponsibleSummaryDTO responsible,


    @Schema(nullable = true, example = "2023-01-01T00:00:00Z", description = "Data e hora quando o aluno foi arquivado")
    Instant archivedAt,

    @Schema(nullable = true,example = "2023-01-01T00:00:00Z",description = "Data e hora quando o aluno foi atualizado")
    Instant updatedAt,

    @NotNull
    @Schema(example = "2023-01-01T00:00:00Z", description = "Data e hora quando o aluno foi criado")
    Instant createdAt
) {}
