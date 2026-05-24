package aprimorar.atendimentos.api.dto;

import aprimorar.shared.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Agenda paginada e resumo financeiro do aluno no periodo consultado")
public record AlunoAtendimentosResponseDTO(
    @Schema(description = "Atendimentos do aluno, paginados e filtrados conforme os parametros informados")
    PageDTO<AtendimentoResponseDTO> atendimentos,

    @Schema(description = "Indicadores do aluno no periodo: total de atendimentos, total cobrado e total pendente")
    AlunoSummaryDTO summary
) {}
