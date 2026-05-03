package com.aprimorar.api.domain.event.dto;

import com.aprimorar.api.enums.EventContent;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Formato de payload para o cadastro e/ou update de um evento")
public record EventRequestDTO(

    @Schema(nullable = false, description = "Descrição do evento", example = "Sessão focada em revisão de matemática básica")
    String description,

    @NotNull(message = "Conteúdo do evento é obrigatório")
    @Schema(nullable = false, description = "Conteúdo do evento (Atendimento, Mentoria, etc...)", example = "Mentoria")
    EventContent content,

    @NotNull(message = "Data/hora de início do evento é obrigatória")
    @Schema(nullable = false, description = "Data/Horário de início do evento", example = "2023-11-20T14:00:00Z")
    Instant startDate,

    @NotNull(message = "A duração do evento é obrigatória")
    @Positive(message = "A duração deve ser maior que zero")
    @Schema(nullable = false, description = "Duração do evento em horas", example = "1.5")
    Double duration,

    @NotNull(message = "Preço é obrigatório")
    @PositiveOrZero(message = "Preço deve ser maior ou igual a 0")
    @Schema(nullable = false, description = "Preço do evento pago pelo aluno", example = "150.00")
    BigDecimal price,

    @NotNull(message = "Pagamento é obrigatório")
    @PositiveOrZero(message = "Pagamento deve ser maior ou igual a 0")
    @Schema(nullable = false, description = "Preço do evento pago ao colaborador", example = "100.00")
    BigDecimal payment,

    @NotNull(message = "ID do estudante é obrigatório")
    @Schema(nullable = false, description = "ID do estudante vinculado ao evento", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID studentId,

    @NotNull(message = "ID do funcionário é obrigatório")
    @Schema(nullable = false, description = "ID do colaborador vinculado ao evento", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID employeeId
) {}
