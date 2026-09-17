package aprimorar.instituicao.atendimentos.web.dto.calendario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Filtros para consultar atendimentos individuais no calendário")
public record AtendimentoIndividualCalendarioFiltroRequest(
    @NotNull(message = "Informe o início do período")
    @Schema(description = "Início do intervalo visível", example = "2026-09-01T00:00:00")
    LocalDateTime inicio,

    @NotNull(message = "Informe o fim do período")
    @Schema(description = "Fim do intervalo visível", example = "2026-09-30T23:59:59")
    LocalDateTime fim,

    @Schema(description = "Filtra os eventos de um aluno", nullable = true)
    UUID alunoId,

    @Schema(description = "Filtra os eventos de um colaborador", nullable = true)
    UUID colaboradorId
) {

    @AssertTrue(message = "O fim do período não pode ser anterior ao início")
    @Schema(hidden = true)
    public boolean periodoValido() {
        return inicio == null || fim == null || !fim.isBefore(inicio);
    }
}
