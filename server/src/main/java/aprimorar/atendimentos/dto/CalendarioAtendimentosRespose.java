package aprimorar.atendimentos.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import aprimorar.atendimentos.enums.TipoAtendimento;
import aprimorar.atendimentos.repository.projections.AtendimentoCalendarioProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Evento simplificado para o calendário de atendimentos")
public record CalendarioAtendimentosRespose(
    @NotNull
    @Schema(description = "Identificador do atendimento", example = "1")
    Long id,
    @NotNull
    @Schema(description = "ID do colaborador", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID colaboradorId,
    @NotNull
    @Schema(description = "ID do aluno", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID alunoId,
    @NotNull
    @Schema(description = "Início do atendimento", example = "2024-03-10T15:33:42")
    LocalDateTime inicio,
    @NotNull
    @Schema(description = "Fim do atendimento", example = "2024-03-10T16:33:42")
    LocalDateTime fim,
    @NotNull
    @Schema(description = "Tipo do atendimento", example = "MENTORIA")
    TipoAtendimento tipo,
    @NotNull
    @Schema(description = "Nome do colaborador", example = "Maria Souza")
    String nomeColaborador,
    @NotNull
    @Schema(description = "Nome do aluno", example = "João Silva")
    String nomeAluno
) {

    public static CalendarioAtendimentosRespose toDto(AtendimentoCalendarioProjection projection) {
        return new CalendarioAtendimentosRespose(
            projection.getId(),
            projection.getColaboradorId(),
            projection.getAlunoId(),
            projection.getDataHoraInicio(),
            projection.getDataHoraFim(),
            projection.getTipo(),
            projection.getNomeColaborador(),
            projection.getNomeAluno()
        );
    }
}
