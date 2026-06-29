package aprimorar.atendimentos.dto;

import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.enums.StatusAtendimento;
import aprimorar.atendimentos.enums.TipoAtendimento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Schema(description = "Dados do atendimento retornados pela API")
public record AtendimentoResponse(
    @NotNull
    @Schema(nullable = false, description = "Identificador unico do atendimento", example = "550e8400-e29b-41d4-a716-446655440000")
    Long id,

    @NotNull
    @Schema(nullable = false, description = "Conteudo do atendimento (aula, mentoria etc.)", example = "MENTORIA")
    TipoAtendimento tipo,

    @NotNull
    @Schema(nullable = false, description = "Data/hora de inicio do atendimento", example = "2023-11-20T14:00:00Z")
    LocalDateTime dataHoraInicio,

    @NotNull
    @Schema(nullable = false, description = "Data/hora de fim do atendimento", example = "2023-11-20T16:00:00Z")
    LocalDateTime dataHoraFim,

    @NotNull
    @Schema(nullable = false, description = "Valor pago pelo aluno", example = "150.00")
    BigDecimal pagamentoAluno,

    @NotNull
    @Schema(nullable = false, description = "Valor de repasse ao colaborador", example = "100.00")
    BigDecimal repasseColaborador,

    @NotNull
    @Schema(nullable = false, description = "ID do aluno vinculado ao atendimento", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID alunoId,

    @NotNull
    @Schema(nullable = false, description = "ID do colaborador vinculado ao atendimento", example = "123e4567-e89b-12d3-a456-426614174001")
    UUID colaboradorId,

    @Nullable
    @Schema(nullable = true, description = "Data do pagamento do aluno", example = "2024-03-10T15:33:42Z")
    LocalDateTime dataPagamentoAluno,

    @Nullable
    @Schema(nullable = true, description = "Data do pagamento do colaborador", example = "2024-03-10T15:33:42Z")
    LocalDateTime dataPagamentoColaborador,

    @NotNull
    @Schema(nullable = false, description = "Status do atendimento", example = "AGENDADO")
    StatusAtendimento status,

    @NotNull
    @Schema(nullable = false, description = "Data de criacao do atendimento", example = "2024-03-10T15:33:42Z")
    LocalDateTime createdAt,

    @Nullable
    @Schema(nullable = true, description = "Data de atualizacao do atendimento", example = "2024-03-10T15:33:42Z")
    LocalDateTime updatedAt
) {
    public static AtendimentoResponse toDto(Atendimento atendimento) {
        return new AtendimentoResponse(
            atendimento.getId(),
            atendimento.getTipo(),
            atendimento.getDataHoraInicio(),
            atendimento.getDataHoraFim(),
            atendimento.getPagamentoAluno(),
            atendimento.getRepasseColaborador(),
            atendimento.getAlunoId(),
            atendimento.getColaboradorId(),
            atendimento.getDataPagamentoAluno(),
            atendimento.getDataPagamentoColaborador(),
            atendimento.getStatus(),
            atendimento.getCreatedAt(),
            atendimento.getUpdatedAt()
        );
    }
}
