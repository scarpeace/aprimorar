package aprimorar.pessoas.dto;

import aprimorar.pessoas.domain.Colaborador;
import aprimorar.pessoas.shared.FuncoesColaborador;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados do colaborador retornados pela API")
public record ColaboradorResponseDTO(
    @NotNull
    @Schema(description = "Identificador unico do colaborador", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,

    @NotNull
    @Schema(description = "Nome completo do colaborador", example = "Joao Pereira")
    String nome,

    @NotNull
    @Schema(description = "Data de nascimento do colaborador", example = "1990-05-21")
    LocalDate dataNascimento,

    @NotNull
    @Schema(description = "Chave PIX do colaborador", example = "joao@pix.com")
    String pix,

    @NotNull
    @Schema(description = "Telefone de contato do colaborador", example = "(61) 99999-9999")
    String telefone,

    @NotNull
    @Schema(description = "CPF do colaborador", example = "123.456.789-00")
    String cpf,

    @NotNull
    @Schema(description = "Email do colaborador", example = "joao@empresa.com")
    String email,

    @NotNull
    @Schema(description = "Funcao do colaborador", example = "PROFESSOR")
    FuncoesColaborador funcao,

    @Schema(implementation = EnderecoResponseDTO.class, description = "Endereco do colaborador")
    @NotNull
    EnderecoResponseDTO endereco,

    @Schema(nullable = false, description = "Indica se o colaborador esta ativo", example = "true")
    boolean active,

    @NotNull
    @Schema(description = "Data de criacao do colaborador", example = "2024-03-10T15:33:42")
    LocalDateTime createdAt,

    @Schema(nullable = true, description = "Data da ultima atualizacao do colaborador", example = "2024-03-11T11:10:00")
    @Nullable
    LocalDateTime updatedAt
) {

    public static ColaboradorResponseDTO toDto(Colaborador colaborador) {
        return new ColaboradorResponseDTO(
            colaborador.getId(),
            colaborador.getNome(),
            colaborador.getDataNascimento(),
            colaborador.getPix(),
            colaborador.getTelefone(),
            colaborador.getCpf(),
            colaborador.getEmail(),
            colaborador.getFuncao(),
            EnderecoResponseDTO.toDto(colaborador.getEndereco()),
            colaborador.getActive(),
            colaborador.getCreatedAt(),
            colaborador.getUpdatedAt()
        );
    }
}
