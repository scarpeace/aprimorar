package aprimorar.atendimentos.repository.projections;

import java.time.LocalDateTime;
import java.util.UUID;

import aprimorar.atendimentos.enums.TipoAtendimento;

public interface AtendimentoCalendarioProjection {

    Long getId();
    UUID getColaboradorId();
    UUID getAlunoId();
    LocalDateTime getDataHoraInicio();
    LocalDateTime getDataHoraFim();
    TipoAtendimento getTipo();
    String getNomeColaborador();
    String getNomeAluno();
}
