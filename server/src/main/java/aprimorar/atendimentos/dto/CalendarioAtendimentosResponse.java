package aprimorar.atendimentos.dto;

import aprimorar.atendimentos.enums.TipoAtendimento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Evento simplificado para o calendário de atendimentos")
public record CalendarioAtendimentosResponse(
    @NotNull
    @Schema(description = "Identificador do atendimento", example = "1")
    Long id,

    @NotNull
    @Schema(description = "ID do aluno", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID alunoId,

    @NotNull
    @Schema(description = "Nome do aluno", example = "João Silva")
    String nomeAluno,

    @NotNull
    @Schema(description = "ID do colaborador", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID colaboradorId,

    @NotNull
    @Schema(description = "Nome do colaborador", example = "Maria Souza")
    String nomeColaborador,

    @NotNull
    @Schema(description = "Início do atendimento", example = "2024-03-10T15:33:42")
    LocalDateTime dataHoraInicio,

    @NotNull
    @Schema(description = "Fim do atendimento", example = "2024-03-10T16:33:42")
    LocalDateTime dataHoraFim,

    @NotNull
    @Schema(description = "Tipo do atendimento", example = "MENTORIA")
    TipoAtendimento tipo
) {
}
