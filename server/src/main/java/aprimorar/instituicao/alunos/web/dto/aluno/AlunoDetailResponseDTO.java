package aprimorar.instituicao.alunos.web.dto.aluno;

import aprimorar.instituicao.alunos.domain.Aluno;
import aprimorar.instituicao.common.web.dto.endereco.EnderecoResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Tag(name = "Aluno", description = "Dados detalhados do aluno")
@Schema(description = "Dados detalhados do aluno")
public record AlunoDetailResponseDTO(
    @NotNull
    @Schema(description = "Identificador único do aluno", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,

    @NotNull
    @Schema(example = "John Doe", description = "Nome do aluno")
    String nome,

    @NotNull
    @Schema(example = "123456789", description = "Contato do aluno")
    String telefone,

    @NotNull
    @Schema(example = "john.doe@example.com", description = "Email do aluno")
    String email,

    @NotNull
    @Schema(example = "123.456.789-00", description = "CPF do aluno")
    String cpf,

    @NotNull
    @Schema(example = "1990-01-01", description = "Data de nascimento do aluno")
    LocalDate dataNascimento,

    @NotNull
    @Schema(example = "Leonardo da Vinci", description = "Nome da escola do aluno")
    String escola,

    @Schema(example = "true", description = "Indica se o aluno está ativo")
    Boolean active,

    @NotNull
    @Schema(description = "Dados do responsável do aluno")
    ResponsavelResponseDTO responsavel,

    @NotNull
    @Schema(implementation = EnderecoResponseDTO.class, description = "Endereço do aluno")
    EnderecoResponseDTO endereco,

    @Schema(nullable = true, example = "2023-01-01T00:00:00", description = "Data e hora da última atualização do aluno")
    LocalDateTime updatedAt,

    @NotNull
    @Schema(example = "2023-01-01T00:00:00", description = "Data e hora de criação do aluno")
    LocalDateTime createdAt
) {
    public static AlunoDetailResponseDTO from(Aluno aluno) {
        return new AlunoDetailResponseDTO(
            aluno.getId(),
            aluno.getNome(),
            aluno.getTelefone(),
            aluno.getEmail(),
            aluno.getCpf(),
            aluno.getDataNascimento(),
            aluno.getEscola(),
            aluno.getActive(),
            ResponsavelResponseDTO.toDto(aluno.getResponsavel()),
            EnderecoResponseDTO.toDto(aluno.getEndereco()),
            aluno.getUpdatedAt(),
            aluno.getCreatedAt()
        );
    }
}
