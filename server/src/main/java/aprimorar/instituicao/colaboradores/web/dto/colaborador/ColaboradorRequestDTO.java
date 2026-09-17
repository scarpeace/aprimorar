package aprimorar.instituicao.colaboradores.web.dto.colaborador;

import java.time.LocalDate;

import aprimorar.common.utils.CpfUtils;
import aprimorar.common.utils.EmailUtils;
import aprimorar.common.utils.PhoneUtils;
import aprimorar.instituicao.colaboradores.domain.enums.FuncoesColaborador;
import aprimorar.instituicao.common.web.dto.endereco.EnderecoRequestDTO;
import aprimorar.instituicao.colaboradores.domain.ColaboradorEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

@Schema(description = "Formato de payload para criar um novo colaborador")
public record ColaboradorRequestDTO(
        @NotBlank(message = "Nome do colaborador é obrigatório")
        @Schema(description = "Nome completo do colaborador", example = "João Pereira")
        String nome,

        @NotNull(message = "Data de nascimento do colaborador é obrigatória")
        @Past(message = "A data de nascimento precisa ser anterior a hoje")
        @Schema(description = "Data de nascimento do colaborador", example = "1990-05-21")
        LocalDate dataNascimento,

        @NotBlank(message = "Chave PIX do colaborador é obrigatória")
        @Schema(description = "Chave PIX do colaborador", example = "joao.pereira@example.com")
        String pix,

        @NotBlank(message = "Contato do colaborador é obrigatório")
        @Schema(description = "Telefone de contato do colaborador", example = "(61) 99999-9999")
        String telefone,

        @NotBlank(message = "CPF do colaborador é obrigatório")
        @Schema(description = "CPF do colaborador", example = "123.456.789-00")
        String cpf,

        @NotBlank(message = "Email do colaborador é obrigatório")
        @Email(message = "Use um e-mail válido")
        @Schema(description = "E-mail do colaborador", example = "joao.pereira@example.com")
        String email,

        @NotNull(message = "Função do colaborador é obrigatória")
        @Schema(description = "Função do colaborador", example = "PROFESSOR")
        FuncoesColaborador funcao,

        @Valid
        @NotNull(message = "Endereço do colaborador é obrigatório")
        @Schema(nullable = false, description = "Endereço do colaborador", implementation = EnderecoRequestDTO.class)
        EnderecoRequestDTO endereco) {

    public ColaboradorEntity toEntity() {
        return new ColaboradorEntity(
            this.nome(),
            this.dataNascimento(),
            this.pix(),
            PhoneUtils.normalize(this.telefone()),
            CpfUtils.normalize(this.cpf()),
            EmailUtils.normalize(this.email()),
            this.funcao(),
            this.endereco().toEntity()
        );
    }
}
