package aprimorar.atendimentos.repository.projections;

import java.time.LocalDateTime;
import java.util.UUID;

import aprimorar.atendimentos.enums.TipoAtendimento;

public interface AtendimentoCalendarioProjection {

    UUID getId();
    UUID getColaboradorId();
    UUID getAlunoId();
    LocalDateTime getInicio();
    LocalDateTime getFim();
    TipoAtendimento getTipo();
    String getNomeColaborador();
    String getNomeAluno();
}
