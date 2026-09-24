package aprimorar.agendamento.alunos.web.dto;

import aprimorar.agendamento.alunos.domain.Aluno;
import aprimorar.agendamento.alunos.domain.Responsavel;
import aprimorar.agendamento.common.web.dto.endereco.EnderecoResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Tag(name = "Alunos", description = "Dados detalhados do aluno")
@Schema(description = "Dados detalhados do aluno")
public record AlunoResponse(
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
    ResponsavelResponse responsavel,

    @NotNull
    @Schema(implementation = EnderecoResponse.class, description = "Endereço do aluno")
    EnderecoResponse endereco,

    @Schema(nullable = true, example = "2023-01-01T00:00:00", description = "Data e hora da última atualização do aluno")
    LocalDateTime updatedAt,

    @NotNull
    @Schema(example = "2023-01-01T00:00:00", description = "Data e hora de criação do aluno")
    LocalDateTime createdAt
) {
    public static AlunoResponse toDto(Aluno aluno) {
        return new AlunoResponse(
            aluno.getId(),
            aluno.getNome(),
            aluno.getTelefone(),
            aluno.getEmail(),
            aluno.getCpf(),
            aluno.getDataNascimento(),
            aluno.getEscola(),
            aluno.getActive(),
            ResponsavelResponse.toDto(aluno.getResponsavel()),
            EnderecoResponse.toDto(aluno.getEndereco()),
            aluno.getUpdatedAt(),
            aluno.getCreatedAt()
        );
    }

    @Schema(description = "Dados do responsável do aluno")
    public record ResponsavelResponse(
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
        public static ResponsavelResponse toDto(Responsavel responsavel) {
            return new ResponsavelResponse(
                responsavel.getNome(),
                responsavel.getCpf(),
                responsavel.getTelefone(),
                responsavel.getEmail()
            );
        }
    }
}
