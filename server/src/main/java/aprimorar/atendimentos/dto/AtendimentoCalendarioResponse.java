package aprimorar.atendimentos.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import aprimorar.atendimentos.enums.TipoAtendimento;
import aprimorar.atendimentos.repository.projections.CalendarioAtendimentoProjection;

public record AtendimentoCalendarioResponse(
    UUID colaboradorId,
    UUID alunoId,
    LocalDateTime inicio,
    LocalDateTime fim,
    TipoAtendimento tipo,
    String nomeColaborador,
    String nomeAluno
) {

    public static AtendimentoCalendarioResponse toDto(CalendarioAtendimentoProjection projection) {
        return new AtendimentoCalendarioResponse(
            projection.getColaboradorId(),
            projection.getAlunoId(),
            projection.getInicio(),
            projection.getFim(),
            projection.getTipo(),
            projection.getNomeColaborador(),
            projection.getNomeAluno()
        );
    }
}
