package aprimorar.atendimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import aprimorar.atendimentos.enums.TipoAtendimentoEnum;

@Schema(description = "Formato de payload para cadastro e atualização de atendimento")
public record AtendimentoRequestDTO(

    @Schema(nullable = false, description = "Descrição do atendimento", example = "Sessão focada em revisão de matemática básica")
    String description,

    @NotNull(message = "Conteudo do atendimento e obrigatorio")
    @Schema(nullable = false, description = "Tipo de conteúdo do atendimento", example = "MENTORIA")
    TipoAtendimentoEnum content,

    @NotNull(message = "Data/hora de inicio do atendimento e obrigatoria")
    @Future(message = "A data/hora de inicio deve ser no futuro")
    @Schema(nullable = false, description = "Data e hora de início do atendimento", example = "2026-11-20T14:00:00Z")
    Instant startDate,

    @NotNull(message = "A duracao do atendimento e obrigatoria")
    @Positive(message = "A duracao deve ser maior que zero")
    @Schema(nullable = false, description = "Duração do atendimento em horas", example = "1.5")
    Double duration,

    @NotNull(message = "Preço é obrigatório")
    @PositiveOrZero(message = "Preço deve ser maior ou igual a 0")
    @Schema(nullable = false, description = "Valor do atendimento cobrado do aluno", example = "150.00")
    BigDecimal price,

    @NotNull(message = "Pagamento é obrigatório")
    @PositiveOrZero(message = "Pagamento deve ser maior ou igual a 0")
    @Schema(nullable = false, description = "Valor de repasse do atendimento ao colaborador", example = "100.00")
    BigDecimal payment,

    @NotNull(message = "ID do estudante é obrigatório")
    @Schema(nullable = false, description = "ID do estudante vinculado ao atendimento", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID studentId,

    @NotNull(message = "ID do colaborador é obrigatório")
    @Schema(nullable = false, description = "ID do colaborador vinculado ao atendimento", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID employeeId
) {}
