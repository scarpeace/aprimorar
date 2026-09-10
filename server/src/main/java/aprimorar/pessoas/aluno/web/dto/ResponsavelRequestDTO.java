package aprimorar.pessoas.colaborador.repository.web.dto;

import aprimorar.common.utils.MapperUtils;
import aprimorar.pessoas.aluno.domain.Responsavel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados do responsável do aluno")
public record ResponsavelRequestDTO(
    @NotBlank(message = "Nome do responsável é obrigatório")
    @Schema(nullable = false, description = "Nome do responsável", example = "João Silva")
    String nome,

    @Email(message = "Use um e-mail válido")
    @NotBlank(message = "Email do responsável é obrigatório")
    @Schema(nullable = false, description = "E-mail do responsável", example = "joao.silva@example.com")
    String email,

    @NotBlank(message = "Contato do responsável é obrigatório")
    @Schema(nullable = false, description = "Contato do responsável", example = "11999999999")
    String telefone,

    @NotBlank(message = "CPF do responsável é obrigatório")
    @Schema(nullable = false, description = "CPF do responsável", example = "12345678901")
    String cpf
) {
    public Responsavel toDomain() {
        return new Responsavel(
            nome,
            MapperUtils.normalizeContact(telefone),
            MapperUtils.normalizeCpf(cpf),
            MapperUtils.normalizeEmail(email)
        );
    }
}
