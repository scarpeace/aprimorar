package aprimorar.atendimentos.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import aprimorar.atendimentos.enums.TipoAtendimento;
import aprimorar.atendimentos.repository.projections.AtendimentoCalendarioProjection;

public record CalendarioAtendimentosRespose(
    UUID id,
    UUID colaboradorId,
    UUID alunoId,
    LocalDateTime inicio,
    LocalDateTime fim,
    TipoAtendimento tipo,
    String nomeColaborador,
    String nomeAluno
) {

    public static CalendarioAtendimentosRespose toDto(AtendimentoCalendarioProjection projection) {
        return new CalendarioAtendimentosRespose(
            projection.getId(),
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
