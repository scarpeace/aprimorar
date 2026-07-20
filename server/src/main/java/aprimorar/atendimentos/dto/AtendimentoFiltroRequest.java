package aprimorar.atendimentos.dto;

import aprimorar.atendimentos.enums.StatusAtendimento;
import aprimorar.atendimentos.enums.TipoAtendimento;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.UUID;

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
    @Schema(description = "Status do atendimento", nullable = true)
    StatusAtendimento status,
    @Schema(description = "Tipo do atendimento", nullable = true)
    TipoAtendimento tipo,
    @Schema(description = "Ocultar atendimentos cobrados", nullable = true)
    Boolean ocultarCobrados,
    @Schema(description = "Ocultar atendimentos pagos", nullable = true)
    Boolean ocultarPagos,
    @Schema(description = "ID do aluno", nullable = true)
    UUID alunoId,
    @Schema(description = "Nome do aluno", nullable = true)
    String alunoNome,
    @Schema(description = "ID do colaborador", nullable = true)
    UUID colaboradorId,
    @Schema(description = "Nome do colaborador", nullable = true)
    String colaboradorNome
) {
}
