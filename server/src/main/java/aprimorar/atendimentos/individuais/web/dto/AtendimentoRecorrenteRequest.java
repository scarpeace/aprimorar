package aprimorar.atendimentos.individuais.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "Payload para cadastro de atendimentos recorrentes semanais")
public record AtendimentoRecorrenteRequest(
    @Valid
    @NotNull(message = "Informe os dados do atendimento")
    @Schema(nullable = false, description = "Dados base do atendimento")
    AtendimentoRequest atendimento,

    @NotNull(message = "Informe a data final da recorrência")
    @Schema(nullable = false, description = "Data final da recorrência", example = "2026-12-20")
    LocalDate dataFimRecorrencia
) {
    @AssertTrue(message = "Data final da recorrência deve ser igual ou posterior à data inicial")
    @Schema(hidden = true)
    public boolean getPeriodoValido() {
        return atendimento == null
            || dataFimRecorrencia == null
            || !dataFimRecorrencia.isBefore(atendimento.dataHoraInicio().toLocalDate());
    }
}
