package aprimorar.agendamento.alunos.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

import aprimorar.agendamento.alunos.domain.Aluno;

@Schema(description = "Dados resumidos do aluno para listagem")
public record AlunoListResponse(
    UUID id,
    String nome,
    String cpf,
    String telefone,
    String escola,
    boolean ativo,
    LocalDateTime createdAt
) {

    public static AlunoListResponse toDto(Aluno aluno) {
        return new AlunoListResponse(
            aluno.getId(),
            aluno.getNome(),
            aluno.getCpf(),
            aluno.getTelefone(),
            aluno.getEscola(),
            aluno.getActive(),
            aluno.getCreatedAt()
        );
    }
}
