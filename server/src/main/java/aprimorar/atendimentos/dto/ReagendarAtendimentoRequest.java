package aprimorar.atendimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Reagendar um atendimento")
public record ReagendarAtendimentoRequest(
    @NotNull(message = "Informe o novo horário")
    @Future(message = "O novo horário precisa ser no futuro")
    @Schema(description = "Novo horário do atendimento", example = "2024-09-10T10:00:00")
    LocalDateTime novoInicio,

    @NotNull(message = "Informe o novo horário de fim")
    @Future(message = "O novo horário de fim precisa ser no futuro")
    @Schema(description = "Novo fim do atendimento", example = "2024-09-10T11:00:00")
    LocalDateTime novoFim
) {}
