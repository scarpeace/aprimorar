package com.aprimorar.api.domain.event.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.aprimorar.api.enums.EventContent;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados do evento retornados pela API")
public record EventResponseDTO(
    @NotNull
    @Schema(nullable = false, description = "Identificador único do evento", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID eventId,

    @Nullable
    @Schema(nullable = false, description = "Descrição do evento", example = "Sessão focada em revisão de matemática básica")
    String description,

    @NotNull
    @Schema(nullable = false, description = "Conteúdo do evento (Atendimento, Mentoria, etc...)", example = "Mentoria")
    EventContent content,

    @NotNull
    @Schema(nullable = false, description = "Data/Horário de início do evento", example = "2023-11-20T14:00:00Z")
    Instant startDate,

    @NotNull
    @Schema(nullable = false, description = "Data/Horário de fim do evento", example = "2023-11-20T16:00:00Z")
    Instant endDate,

    @NotNull
    @Schema(nullable = false, description = "Duração do evento em horas", example = "1.5")
    Double duration,

    @NotNull
    @Schema(nullable = false, description = "Preço do evento pago pelo aluno", example = "150.00")
    BigDecimal price,

    @NotNull
    @Schema(nullable = false, description = "Preço do evento pago ao colaborador", example = "100.00")
    BigDecimal payment,

    @NotNull
    @Schema(nullable = false, description = "ID do estudante vinculado ao evento", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID studentId,

    @NotNull
    @Schema(nullable = false, description = "Nome do aluno vinculado ao evento", example = "John Doe")
    String studentName,

    @NotNull
    @Schema(nullable = false, description = "ID do colaborador vinculado ao evento", example = "123e4567-e89b-12d3-a456-426614174001")
    UUID employeeId,

    @NotNull
    @Schema(nullable = false, description = "Nome do colaborador vinculado ao evento", example = "Jane Doe")
    String employeeName,

    @NotNull
    @Schema(nullable = false, description = "Indica se a cobrança do aluno já foi baixada", example = "false")
    boolean studentCharged,

    @NotNull
    @Schema(nullable = false, description = "Indica se o pagamento do colaborador já foi baixado", example = "false")
    boolean employeePaid,

    @NotNull
    @Schema(nullable = false, description = "Data de criação do evento", example = "2024-03-10T15:33:42Z`")
    Instant createdAt,

    @Nullable
    @Schema(nullable = true, description = "Data de atualização do evento", example = "2024-03-10T15:33:42Z`")
    Instant updatedAt
) {}
