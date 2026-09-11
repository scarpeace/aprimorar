package aprimorar.atendimentos.individuais.web.dto.calendario;

import aprimorar.atendimentos.individuais.domain.AtendimentoIndividualViewEntity;
import aprimorar.atendimentos.individuais.domain.enums.TipoAtendimento;
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
    String colaboradorNome,

    @NotNull
    @Schema(description = "Status da cobrança")
    String statusCobranca,

    @NotNull
    @Schema(description = "Status do repasse")
    String statusRepasse
) {

    public static AtendimentoIndividualCalendarioResponse toDto(AtendimentoIndividualViewEntity atendimento) {
        return new AtendimentoIndividualCalendarioResponse(
            atendimento.getId(),
            atendimento.getDataHoraInicio(),
            atendimento.getDataHoraFim(),
            atendimento.getTipo(),
            atendimento.getAlunoId(),
            atendimento.getAlunoNome(),
            atendimento.getColaboradorId(),
            atendimento.getColaboradorNome(),
            atendimento.getCobrancaStatus(),
            atendimento.getRepasseStatus()
        );
    }
}
