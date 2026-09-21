package aprimorar.agendamento.atendimentos_particular.web.dto.atendimento;

import aprimorar.agendamento.atendimentos_particular.domain.enums.StatusAtendimentoParticular;
import aprimorar.agendamento.atendimentos_particular.domain.enums.TipoAtendimentoParticular;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Filtros opcionais para listar atendimentos particulares")
public record AtendimentoParticularFiltroRequest(
    @Schema(description = "Texto livre de busca", nullable = true)
    String busca,

    @Schema(description = "Data inicial", format = "date-time", nullable = true)
    LocalDateTime inicio,

    @Schema(description = "Data final", format = "date-time", nullable = true)
    LocalDateTime fim,

    @Schema(description = "Tipo do atendimento", nullable = true)
    TipoAtendimentoParticular tipo,

    @Schema(description = "Status do atendimento", nullable = true)
    StatusAtendimentoParticular status,

    @Schema(description = "ID do aluno", nullable = true)
    UUID alunoId,

    @Schema(description = "ID do colaborador", nullable = true)
    UUID colaboradorId
) {

    @AssertTrue(message = "O fim do período não pode ser anterior ao início")
    @Schema(hidden = true)
    public boolean periodoValido() {
        return inicio == null || fim == null || !fim.isBefore(inicio);
    }
}
