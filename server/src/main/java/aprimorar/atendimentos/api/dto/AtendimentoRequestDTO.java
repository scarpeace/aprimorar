package aprimorar.atendimentos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import aprimorar.atendimentos.internal.TipoAtendimentoEnum;

@Schema(description = "Formato de payload para o cadastro e/ou update de um appointment")
public record AtendimentoRequestDTO(

    @Schema(nullable = false, description = "Descrição do appointment", example = "Sessão focada em revisão de matemática básica")
    String description,

    @NotNull(message = "Conteúdo do appointment é obrigatório")
    @Schema(nullable = false, description = "Conteúdo do appointment (Atendimento, Mentoria, etc...)", example = "Mentoria")
    TipoAtendimentoEnum content,

    @NotNull(message = "Data/hora de início do appointment é obrigatória")
    @Schema(nullable = false, description = "Data/Horário de início do appointment", example = "2023-11-20T14:00:00Z")
    Instant startDate,

    @NotNull(message = "A duração do appointment é obrigatória")
    @Positive(message = "A duração deve ser maior que zero")
    @Schema(nullable = false, description = "Duração do appointment em horas", example = "1.5")
    Double duration,

    @NotNull(message = "Preço é obrigatório")
    @PositiveOrZero(message = "Preço deve ser maior ou igual a 0")
    @Schema(nullable = false, description = "Preço do appointment pago pelo aluno", example = "150.00")
    BigDecimal price,

    @NotNull(message = "Pagamento é obrigatório")
    @PositiveOrZero(message = "Pagamento deve ser maior ou igual a 0")
    @Schema(nullable = false, description = "Preço do appointment pago ao colaborador", example = "100.00")
    BigDecimal payment,

    @NotNull(message = "ID do estudante é obrigatório")
    @Schema(nullable = false, description = "ID do estudante vinculado ao appointment", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID studentId,

    @NotNull(message = "ID do funcionário é obrigatório")
    @Schema(nullable = false, description = "ID do colaborador vinculado ao appointment", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID employeeId
) {}
