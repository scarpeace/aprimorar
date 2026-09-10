package aprimorar.pessoas.aluno.web.dto;

import aprimorar.pessoas.aluno.domain.Responsavel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados do responsável do aluno")
public record ResponsavelResponseDTO(
    @NotNull
    @Schema(description = "Nome do responsável", example = "João Silva")
    String nome,

    @NotNull
    @Schema(description = "CPF do responsável", example = "12345678901")
    String cpf,

    @NotNull
    @Schema(description = "Contato do responsável", example = "11999999999")
    String telefone,

    @NotNull
    @Schema(description = "Email do responsável", example = "email@email.com")
    String email
) {
    public static ResponsavelResponseDTO toDto(Responsavel responsavel) {
        return new ResponsavelResponseDTO(
            responsavel.getNome(),
            responsavel.getCpf(),
            responsavel.getTelefone(),
            responsavel.getEmail()
        );
    }
}
