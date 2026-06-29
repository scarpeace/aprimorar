package aprimorar.pessoas.dto.aluno;

import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.dto.EnderecoResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

@Tag(name = "Aluno", description = "Dados do aluno retornados pela API")
@Schema(description = "Dados do aluno retornados pela API")
public record AlunoResponseDTO(
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
        @NotNull
        @Schema(example = "30", description = "Idade do aluno")
        Integer idade,
        @NotNull
        @Schema(example = "eb4be7ae-ebc6-423a-a380-da97f4a81511", description = "ID do responsável")
        UUID responsavelId,
        @Schema(example = "true", description = "Indica se o aluno está ativo")
        Boolean active,
        @Schema(nullable = true, example = "2023-01-01T00:00:00", description = "Data e hora da última atualização do aluno")
        LocalDateTime updatedAt,
        @NotNull
        @Schema(example = "2023-01-01T00:00:00", description = "Data e hora de criação do aluno")
        LocalDateTime createdAt,
        @NotNull
        @Schema(implementation = EnderecoResponseDTO.class, description = "Endereço do aluno")
        EnderecoResponseDTO endereco
) {
    public static AlunoResponseDTO toDto(Aluno aluno) {
        return new AlunoResponseDTO(
            aluno.getId(),
            aluno.getNome(),
            aluno.getTelefone(),
            aluno.getEmail(),
            aluno.getCpf(),
            aluno.getDataNascimento(),
            aluno.getEscola(),
            calculateAge(aluno.getDataNascimento()),
            aluno.getResponsavelId(),
            aluno.getActive(),
            aluno.getUpdatedAt(),
            aluno.getCreatedAt(),
            EnderecoResponseDTO.toDto(aluno.getEndereco())
        );
    }

    private static Integer calculateAge(LocalDate dataNascimento) {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }
}
