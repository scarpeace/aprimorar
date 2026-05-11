package aprimorar.appointment.api.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import aprimorar.appointment.api.AppointmentContent;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados do appointment retornados pela API")
public record AppointmentResponseDTO(
    @NotNull
    @Schema(nullable = false, description = "Identificador único do appointment", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,

    @Nullable
    @Schema(nullable = false, description = "Descrição do appointment", example = "Sessão focada em revisão de matemática básica")
    String description,

    @NotNull
    @Schema(nullable = false, description = "Conteúdo do appointment (Atendimento, Mentoria, etc...)", example = "Mentoria")
    AppointmentContent content,

    @NotNull
    @Schema(nullable = false, description = "Data/Horário de início do appointment", example = "2023-11-20T14:00:00Z")
    Instant startDate,

    @NotNull
    @Schema(nullable = false, description = "Data/Horário de fim do appointment", example = "2023-11-20T16:00:00Z")
    Instant endDate,

    @NotNull
    @Schema(nullable = false, description = "Duração do appointment em horas", example = "1.5")
    Double duration,

    @NotNull
    @Schema(nullable = false, description = "Preço do appointment pago pelo aluno", example = "150.00")
    BigDecimal price,

    @NotNull
    @Schema(nullable = false, description = "Preço do appointment pago ao colaborador", example = "100.00")
    BigDecimal payment,

    @NotNull
    @Schema(nullable = false, description = "Lucro do appointment", example = "50.00")
    BigDecimal profit,

    @NotNull
    @Schema(nullable = false, description = "ID do estudante vinculado ao appointment", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID studentId,

    @NotNull
    @Schema(nullable = false, description = "Nome do aluno vinculado ao appointment", example = "John Doe")
    String studentName,

    @NotNull
    @Schema(nullable = false, description = "ID do colaborador vinculado ao appointment", example = "123e4567-e89b-12d3-a456-426614174001")
    UUID employeeId,

    @NotNull
    @Schema(nullable = false, description = "Nome do colaborador vinculado ao appointment", example = "Jane Doe")
    String employeeName,

    @Nullable
    @Schema(nullable = true, description = "Data de pagamento ao colaborador", example = "2024-03-10T15:33:42Z")
    Instant employeePaymentDate,

    @Nullable
    @Schema(nullable = true, description = "Data de cobrança do aluno", example = "2024-03-10T15:33:42Z")
    Instant studentChargeDate,

    @NotNull
    @Schema(nullable = false, description = "Data de criação do appointment", example = "2024-03-10T15:33:42Z`")
    Instant createdAt,

    @Nullable
    @Schema(nullable = true, description = "Data de atualização do appointment", example = "2024-03-10T15:33:42Z`")
    Instant updatedAt
) {}
