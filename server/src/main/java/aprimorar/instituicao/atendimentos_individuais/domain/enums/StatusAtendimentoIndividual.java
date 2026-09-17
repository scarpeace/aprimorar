package aprimorar.instituicao.atendimentos_individuais.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status do atendimento individual")
public enum StatusAtendimentoIndividual {
    @Schema(description = "Atendimento agendado")
    AGENDADO,

    @Schema(description = "Atendimento realizado")
    REALIZADO,

    @Schema(description = "Atendimento cancelado")
    CANCELADO
}
