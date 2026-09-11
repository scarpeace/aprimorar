package aprimorar.pessoas.colaborador.web.dto;

import aprimorar.pessoas.colaborador.domain.ColaboradorEntity;
import aprimorar.pessoas.colaborador.enums.FuncoesColaborador;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Dados resumidos do colaborador para listagem")
public record ColaboradorListResponseDTO(
    UUID id,
    String nome,
    FuncoesColaborador funcao,
    boolean ativo
) {

    public static ColaboradorListResponseDTO from(ColaboradorEntity colaborador) {
        return new ColaboradorListResponseDTO(
            colaborador.getId(),
            colaborador.getNome(),
            colaborador.getFuncao(),
            colaborador.getActive()
        );
    }
}
