package aprimorar.pessoas.dto.responsavel;

import aprimorar.pessoas.domain.Responsavel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados do responsável retornados pela API")
public record ResponsavelResponseDTO(
        @NotNull
        @Schema(description = "ID do responsável", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @NotNull
        @Schema(description = "Nome do responsável", example = "João Silva")
        String nome,

        @Schema(description = "Data de nascimento do responsável", example = "1990-01-01", nullable = true)
        LocalDate dataNascimento,

        @NotNull
        @Schema(description = "CPF do responsável", example = "12345678901")
        String cpf,

        @NotNull
        @Schema(description = "Contato do responsável", example = "11999999999")
        String telefone,

        @NotNull
        @Schema(description = "Email do responsável", example = "email@email.com")
        String email,

        @NotNull
        @Schema(description = "Data e hora quando o responsável foi criado", example = "2023-01-01T00:00:00")
        LocalDateTime createdAt,

        @Schema(nullable = true, description = "Data e hora quando o responsável foi atualizado", example = "2023-01-01T00:00:00")
        LocalDateTime updatedAt
        ) {

        public static ResponsavelResponseDTO toDto(Responsavel responsavel) {
                return new ResponsavelResponseDTO(
                    responsavel.getId(),
                    responsavel.getNome(),
                    responsavel.getDataNascimento(),
                    responsavel.getCpf(),
                    responsavel.getTelefone(),
                    responsavel.getEmail(),
                    responsavel.getCreatedAt(),
                    responsavel.getUpdatedAt()
                );
        }
}
