package aprimorar.agendamento.colaboradores.web.dto.colaborador;

import aprimorar.agendamento.colaboradores.domain.enums.FuncoesColaborador;
import aprimorar.agendamento.colaboradores.domain.Colaborador;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Dados resumidos do colaborador para listagem")
public record ColaboradorListResponse(
    UUID id,
    String nome,
    FuncoesColaborador funcao,
    boolean ativo
) {

    public static ColaboradorListResponse toDto(Colaborador colaborador) {
        return new ColaboradorListResponse(
            colaborador.getId(),
            colaborador.getNome(),
            colaborador.getFuncao(),
            colaborador.getActive()
        );
    }
}
