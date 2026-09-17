package aprimorar.instituicao.colaboradores.web.dto.colaborador;

import aprimorar.instituicao.colaboradores.domain.enums.FuncoesColaborador;
import aprimorar.instituicao.colaboradores.domain.ColaboradorEntity;
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
