package aprimorar.pessoas.dto.aluno;

import java.time.LocalDate;
import java.util.UUID;

import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.dto.EnderecoRequestDTO;
import aprimorar.utils.MapperUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Schema(description = "Formato de payload para o cadastro de um aluno")
public record AlunoRequestDTO(
    @NotBlank(message = "Nome do aluno é obrigatório")
    @Schema(nullable = false, description = "Nome completo do aluno", example = "Ana Silva")
    String nome,

    @NotNull(message = "Data de nascimento do aluno é obrigatória")
    @PastOrPresent(message = "A data de nascimento não pode ser futura")
    @Schema(nullable = false, description = "Data de nascimento do aluno", example = "2000-01-01")
    LocalDate dataNascimento,

    @NotBlank(message = "O CPF do aluno é obrigatório")
    @Schema(nullable = false, description = "CPF do aluno", example = "123.456.789-00")
    String cpf,

    @NotBlank(message = "A escola do aluno é obrigatória")
    @Schema(nullable = false,description = "Escola do aluno", example = "Colégio Aprimorar")
    String escola,

    @NotBlank(message = "Contato do aluno é obrigatório")
    @Schema(nullable = false,description = "Contato do aluno", example = "(61) 99999-9999")
    String telefone,

    @NotBlank(message = "Email do aluno é obrigatório")
    @Email(message = "Use um e-mail válido")
    @Schema(nullable = false,description = "E-mail do aluno", example = "ana.silva@example.com")
    String email,

    @Valid
    @NotNull(message = "Endereço do aluno é obrigatório")
    @Schema(nullable = false,description = "Endereço do aluno", implementation = EnderecoRequestDTO.class)
    EnderecoRequestDTO endereco,

    @NotNull(message = "Aluno não pode ser criado sem um responsável")
    @Schema(nullable = false,description = "ID do responsável vinculado ao aluno")
    UUID responsavelId
) {
    public Aluno toEntity() {
        return new Aluno(
            this.nome(),
            this.dataNascimento(),
            MapperUtils.normalizeContact(this.telefone()),
            MapperUtils.normalizeCpf(this.cpf()),
            MapperUtils.normalizeEmail(this.email()),
            this.escola(),
            this.responsavelId(),
            this.endereco().toEntity()
        );
    }
}
