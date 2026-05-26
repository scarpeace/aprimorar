package aprimorar.atendimentos.api.dto;

import aprimorar.shared.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Agenda paginada e resumo financeiro do colaborador no periodo consultado")
public record AtendimentosColaboradorResponseDTO(
    @Schema(description = "Atendimentos do colaborador, paginados e filtrados conforme os parametros informados")
    PageDTO<AtendimentoResponseDTO> atendimentos,

    @Schema(description = "Indicadores do colaborador no periodo: total de atendimentos, total pago e total em aberto")
    ColaboradorSummaryDTO summary
) {}
