package aprimorar.instituicao.atendimentos_individuais.web.dto.atendimento;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

import java.util.UUID;

import aprimorar.instituicao.atendimentos_individuais.domain.enums.TipoAtendimento;

@Schema(description = "Filtros opcionais para listar atendimentos")
public record AtendimentoIndividualFiltroRequest(
    @Schema(description = "Texto livre de busca", nullable = true)
    String busca,

    @Schema(description = "Data inicial", format = "date-time", nullable = true)
    LocalDateTime inicio,
    @Schema(description = "Data final", format = "date-time", nullable = true)
    LocalDateTime fim,
    @Schema(description = "Tipo do atendimento", nullable = true)
    TipoAtendimento tipo,
    @Schema(description = "ID do aluno", nullable = true)
    UUID alunoId,
    @Schema(description = "ID do colaborador", nullable = true)
    UUID colaboradorId
) {
}
