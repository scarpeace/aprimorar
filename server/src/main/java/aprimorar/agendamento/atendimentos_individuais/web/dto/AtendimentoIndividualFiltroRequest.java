package aprimorar.agendamento.atendimentos_individuais.web.dto;

import aprimorar.agendamento.atendimentos_individuais.domain.enums.StatusAtendimentoIndividual;
import aprimorar.agendamento.atendimentos_individuais.domain.enums.TipoAtendimentoIndividual;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Filtros opcionais para listar atendimentos individuais")
public record AtendimentoIndividualFiltroRequest(
    @Schema(description = "Texto livre de busca", nullable = true)
    String busca,

    @Schema(description = "Data inicial", format = "date-time", nullable = true)
    LocalDateTime inicio,

    @Schema(description = "Data final", format = "date-time", nullable = true)
    LocalDateTime fim,

    @Schema(description = "Tipo do atendimento", nullable = true)
    TipoAtendimentoIndividual tipo,

    @Schema(description = "Status do atendimento", nullable = true)
    StatusAtendimentoIndividual status,

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
