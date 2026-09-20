package aprimorar.agendamento.alunos.web.dto.aluno;

import java.time.LocalDate;
import aprimorar.common.utils.CpfUtils;
import aprimorar.common.utils.EmailUtils;
import aprimorar.common.utils.PhoneUtils;
import aprimorar.agendamento.alunos.domain.Aluno;
import aprimorar.agendamento.alunos.domain.Responsavel;
import aprimorar.agendamento.common.web.dto.endereco.EnderecoRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Schema(description = "Formato de payload para o cadastro de um aluno")
public record AlunoRequest(
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
    @Schema(nullable = false,description = "Endereço do aluno", implementation = EnderecoRequest.class)
    EnderecoRequest endereco,

    @Valid
    @NotNull(message = "Aluno não pode ser criado sem um responsável")
    @Schema(nullable = false, description = "Dados do responsável do aluno", implementation = ResponsavelRequest.class)
    ResponsavelRequest responsavel
) {
    public Aluno toEntity() {
        return new Aluno(
            this.nome(),
            this.dataNascimento(),
            PhoneUtils.normalize(this.telefone()),
            CpfUtils.normalize(this.cpf()),
            EmailUtils.normalize(this.email()),
            this.escola(),
            this.responsavel().toDomain(),
            this.endereco().toEntity()
        );
    }

    @Schema(description = "Dados do responsável do aluno")
    public record ResponsavelRequest(
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
                PhoneUtils.normalize(telefone),
                CpfUtils.normalize(cpf),
                EmailUtils.normalize(email)
            );
        }
    }
}
