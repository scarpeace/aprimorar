package aprimorar.pessoas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(description = "Opção simplificada de responsável para seletores")
public record ResponsaveisListDTO(
    @NotNull @Schema(description = "Identificador único do responsável", example = "123e4567-e89b-12d3-a456-426614174000") UUID id,
    @NotNull @Schema(description = "Nome completo do responsável", example = "João Silva") String name
) {}
