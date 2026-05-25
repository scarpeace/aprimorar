package aprimorar.pessoas.aluno.api.dto;


import org.springframework.data.domain.Page;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo financeiro e de atendimentos de um aluno")
public record AlunosResponseDTO(

//TODO: Adicionar informações do OPENAPI aqui.
  @Schema(description = "Total de alunos ativos")
  Long totalActiveStudents,

  @Schema(description = "Lista de alunos")
  Page<AlunoResponseDTO> alunos
) {
}
