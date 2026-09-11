package aprimorar.pessoas.web.dto.aluno;

import aprimorar.pessoas.domain.AlunoEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Dados resumidos do aluno para listagem")
public record AlunoListResponseDTO(
    UUID id,
    String nome,
    String escola,
    boolean ativo
) {

    public static AlunoListResponseDTO from(AlunoEntity aluno) {
        return new AlunoListResponseDTO(
            aluno.getId(),
            aluno.getNome(),
            aluno.getEscola(),
            aluno.getActive()
        );
    }
}
