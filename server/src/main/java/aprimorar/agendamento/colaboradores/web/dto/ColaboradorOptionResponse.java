package aprimorar.agendamento.colaboradores.web.dto;

import aprimorar.agendamento.colaboradores.domain.Colaborador;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(description = "Opção simplificada de colaborador para seletores")
public record ColaboradorOptionResponse(
    @NotNull
    @Schema(description = "Identificador único do colaborador", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,

    @NotNull
    @Schema(description = "Nome completo do colaborador", example = "João Pereira")
    String nome
) {

    public static ColaboradorOptionResponse toDto(Colaborador colaborador) {
        return new ColaboradorOptionResponse(
            colaborador.getId(),
            colaborador.getNome()
        );
    }
}
