package aprimorar.pessoas.responsavel.internal.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(description = "Lista de responsáveis para dropdown retornados da API")
public record ResponsaveisListDTO(
    @NotNull @Schema(description = "Identificador único do responsável") UUID id,
    @NotNull @Schema(description = "Nome do responsável") String name
) {}
