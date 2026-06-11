package aprimorar.atendimentos.dto;

import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.enums.TipoAtendimentoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Dados do atendimento retornados pela API")
public record AtendimentoResponseDTO(
    @NotNull
    @Schema(nullable = false, description = "Identificador unico do atendimento", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,

    @Nullable
    @Schema(nullable = false, description = "Descricao do atendimento", example = "Sessao focada em revisao de matematica basica")
    String descricao,

    @NotNull
    @Schema(nullable = false, description = "Conteudo do atendimento (aula, mentoria etc.)", example = "MENTORIA")
    TipoAtendimentoEnum tipo,

    @NotNull
    @Schema(nullable = false, description = "Data/hora de inicio do atendimento", example = "2023-11-20T14:00:00Z")
    Instant inicio,

    @NotNull
    @Schema(nullable = false, description = "Data/hora de fim do atendimento", example = "2023-11-20T16:00:00Z")
    Instant fim,

    @NotNull
    @Schema(nullable = false, description = "Duracao do atendimento em horas", example = "1.5")
    Double duracao,

    @NotNull
    @Schema(nullable = false, description = "Valor do atendimento cobrado do aluno", example = "150.00")
    BigDecimal valor,

    @NotNull
    @Schema(nullable = false, description = "Pagamento do atendimento ao colaborador", example = "100.00")
    BigDecimal repasse,

    @NotNull
    @Schema(nullable = false, description = "Lucro do atendimento", example = "50.00")
    BigDecimal lucro,

    @NotNull
    @Schema(nullable = false, description = "ID do aluno vinculado ao atendimento", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID alunoId,

    @NotNull
    @Schema(nullable = false, description = "Nome do aluno vinculado ao atendimento", example = "John Doe")
    String alunoNome,

    @NotNull
    @Schema(nullable = false, description = "ID do colaborador vinculado ao atendimento", example = "123e4567-e89b-12d3-a456-426614174001")
    UUID colaboradorId,

    @NotNull
    @Schema(nullable = false, description = "Nome do colaborador vinculado ao atendimento", example = "Jane Doe")
    String colaboradorNome,

    @Nullable
    @Schema(nullable = true, description = "Data de pagamento ao colaborador", example = "2024-03-10T15:33:42Z")
    Instant dataPagamentoColaborador,

    @Nullable
    @Schema(nullable = true, description = "Data de cobrança do aluno", example = "2024-03-10T15:33:42Z")
    Instant dataCobrancaAluno,

    @NotNull
    @Schema(nullable = false, description = "Data de criacao do atendimento", example = "2024-03-10T15:33:42Z")
    Instant createdAt,

    @Nullable
    @Schema(nullable = true, description = "Data de atualizacao do atendimento", example = "2024-03-10T15:33:42Z")
    Instant updatedAt
) {
    public static AtendimentoResponseDTO toDto(Atendimento atendimento) {
        return new AtendimentoResponseDTO(
            atendimento.getId(),
            atendimento.getDescricao(),
            atendimento.getTipo(),
            atendimento.getInicio(),
            atendimento.getFim(),
            atendimento.getDuracao(),
            atendimento.getValor(),
            atendimento.getRepasse(),
            atendimento.getLucro(),
            atendimento.getAlunoId(),
            atendimento.getAlunoNome(),
            atendimento.getColaboradorId(),
            atendimento.getColaboradorNome(),
            atendimento.getDataPagamentoColaborador(),
            atendimento.getDataCobrancaAluno(),
            atendimento.getCreatedAt(),
            atendimento.getUpdatedAt()
        );
    }
}
