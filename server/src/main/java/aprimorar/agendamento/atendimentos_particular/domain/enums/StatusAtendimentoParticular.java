package aprimorar.agendamento.atendimentos_particular.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status do atendimento particular")
public enum StatusAtendimentoParticular {
    @Schema(description = "Atendimento agendado")
    AGENDADO,

    @Schema(description = "Atendimento realizado")
    REALIZADO,

    @Schema(description = "Atendimento cancelado")
    CANCELADO
}
