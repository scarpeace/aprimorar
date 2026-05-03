package com.aprimorar.api.domain.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(description = "Resumo do responsável vinculado ao aluno")
public record StudentResponsibleSummaryDTO(
    @NotNull
    @Schema(description = "ID do responsável", example = "eb4be7ae-ebc6-423a-a380-da97f4a81511")
    UUID responsibleId,

    @NotBlank
    @Schema(description = "Nome do responsável", example = "Maria Silva")
    String name,

    @NotBlank
    @Schema(description = "Contato do responsável", example = "61977777777")
    String contact,

    @NotBlank
    @Schema(description = "CPF do responsável", example = "98765432100")
    String cpf
) {}
