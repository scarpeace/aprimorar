package aprimorar.instituicao.alunos.web.dto.aluno;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

import aprimorar.instituicao.alunos.domain.AlunoEntity;

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
