package aprimorar.atendimentos.individuais.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.UUID;

import aprimorar.atendimentos.individuais.enums.TipoAtendimento;

@Schema(description = "Filtros opcionais para listar atendimentos")
public record AtendimentoFiltroRequest(
    @Schema(description = "Texto livre de busca", nullable = true)
    String busca,
    @Schema(description = "Ano e mes do filtro", example = "2026-06", nullable = true)
    YearMonth anoMes,
    @Schema(description = "Data inicial", format = "date-time", nullable = true)
    LocalDateTime inicio,
    @Schema(description = "Data final", format = "date-time", nullable = true)
    LocalDateTime fim,
    @Schema(description = "Tipo do atendimento", nullable = true)
    TipoAtendimento tipo,
    @Schema(description = "ID do aluno", nullable = true)
    UUID alunoId,
    @Schema(description = "ID do colaborador", nullable = true)
    UUID colaboradorId,
    @Schema(description = "Status da cobrança", example = "PENDENTE", nullable = true)
    String statusCobranca
) {
}
