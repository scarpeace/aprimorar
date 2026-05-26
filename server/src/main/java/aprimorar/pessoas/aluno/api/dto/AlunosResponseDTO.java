package aprimorar.pessoas.aluno.api.dto;

import aprimorar.shared.PageDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo e listagem paginada de alunos")
public record AlunosResponseDTO(
  @Schema(description = "Total de alunos ativos", example = "42")
  Long totalActiveStudents,

  @Schema(description = "Página de alunos conforme filtros aplicados")
  PageDTO<AlunoResponseDTO> alunos
) {
}
