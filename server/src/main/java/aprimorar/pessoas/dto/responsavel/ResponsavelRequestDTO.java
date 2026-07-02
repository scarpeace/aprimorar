package aprimorar.pessoas.dto.responsavel;

import java.time.LocalDate;

import aprimorar.pessoas.domain.Responsavel;
import aprimorar.utils.MapperUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Formato de payload para criar um novo responsável")
public record ResponsavelRequestDTO(
        @NotBlank(message = "Nome do responsável é obrigatório")
        @Schema(nullable = false, description = "Nome do responsável", example = "João Silva")
        String nome,

        @NotBlank(message = "Email do responsável é obrigatório")
        @Email(message = "Use um e-mail válido")
        @Schema(nullable = false, description = "E-mail do responsável", example = "joao.silva@example.com")
        String email,

        @NotBlank(message = "Contato do responsável é obrigatório")
        @Schema(nullable = false, description = "Contato do responsável", example = "11999999999")
        String telefone,

        @Schema(nullable = true, description = "Data de nascimento do responsável", example = "1990-01-01")
        LocalDate dataNascimento,

        @NotBlank(message = "CPF do responsável é obrigatório")
        @Schema(nullable = false, description = "CPF do responsável", example = "12345678901")
        String cpf
) {
    public Responsavel toEntity() {
        return new Responsavel(
            this.nome(),
            this.dataNascimento(),
            MapperUtils.normalizeContact(this.telefone()),
            MapperUtils.normalizeCpf(this.cpf()),
            MapperUtils.normalizeEmail(this.email())
        );
    }
}
