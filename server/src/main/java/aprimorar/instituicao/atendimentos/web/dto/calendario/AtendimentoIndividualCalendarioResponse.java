package aprimorar.instituicao.atendimentos.web.dto.calendario;

import aprimorar.instituicao.atendimentos.domain.AtendimentoIndividualEntity;
import aprimorar.instituicao.atendimentos.domain.enums.StatusAtendimentoIndividual;
import aprimorar.instituicao.atendimentos.domain.enums.TipoAtendimento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Atendimento individual no formato de consulta de calendário")
public record AtendimentoIndividualCalendarioResponse(
    @NotNull
    @Schema(description = "Identificador do atendimento")
    Long id,

    @NotNull
    @Schema(description = "Data/hora de início")
    LocalDateTime dataHoraInicio,

    @NotNull
    @Schema(description = "Data/hora de fim")
    LocalDateTime dataHoraFim,

    @NotNull
    @Schema(description = "Tipo do atendimento")
    TipoAtendimento tipo,

    @NotNull
    @Schema(description = "Status do atendimento")
    StatusAtendimentoIndividual status,

    @NotNull
    @Schema(description = "Identificador do aluno")
    UUID alunoId,

    @NotNull
    @Schema(description = "Nome do aluno")
    String alunoNome,

    @NotNull
    @Schema(description = "Identificador do colaborador")
    UUID colaboradorId,

    @NotNull
    @Schema(description = "Nome do colaborador")
    String colaboradorNome
) {

    public static AtendimentoIndividualCalendarioResponse from(AtendimentoIndividualEntity atendimento) {
        return new AtendimentoIndividualCalendarioResponse(
            atendimento.getId(),
            atendimento.getDataHoraInicio(),
            atendimento.getDataHoraFim(),
            atendimento.getTipo(),
            atendimento.getStatus(),
            atendimento.getAluno().getId(),
            atendimento.getAluno().getNome(),
            atendimento.getColaborador().getId(),
            atendimento.getColaborador().getNome()
        );
    }
}
