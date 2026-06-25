package aprimorar.atendimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

@Schema(description = "Reagendar um atendimento")
public record ReagendarAtendimentoRequest(
    @NotNull(message = "Informe o novo horário")
    @Future(message = "O novo horário precisa ser no futuro")
    @Schema(description = "Novo horário do atendimento", example = "2024-09-10T10:00:00")
    LocalDateTime novoInicio,

    @NotNull(message = "Informe a duração")
    @Positive(message = "A duração precisa ser maior que zero")
    @Schema(description = "Duração do atendimento", example = "60.0")
    Double duracao
) {}
