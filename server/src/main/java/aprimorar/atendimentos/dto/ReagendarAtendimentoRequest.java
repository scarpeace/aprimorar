package aprimorar.atendimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Reagendar um atendimento")
public record ReagendarAtendimentoRequest(
    @Schema(description = "Novo inicio do atendimento", example = "2024-09-10T10:00:00")
    LocalDateTime novoInicio,
    @Schema(description = "Duracao do atendimento", example = "60.0")
    Double duracao
) {}
