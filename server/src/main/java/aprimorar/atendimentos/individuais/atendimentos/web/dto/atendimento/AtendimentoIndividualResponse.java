package aprimorar.atendimentos.individuais.atendimentos.web.dto.atendimento;

import aprimorar.atendimentos.individuais.atendimentos.domain.AtendimentoIndividualViewEntity;
import aprimorar.atendimentos.individuais.atendimentos.domain.enums.TipoAtendimento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados do atendimento retornados pela API")
public record AtendimentoIndividualResponse(
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
    @Schema(nullable = false, description = "Resumo do aluno vinculado ao atendimento")
    AlunoResumo alunoResumo,

    @NotNull
    @Schema(nullable = false, description = "Resumo do colaborador vinculado ao atendimento")
    ColaboradorResumo colaboradorResumo,

    @NotNull
    @Schema(nullable = false, description = "Resumo da cobrança do atendimento")
    CobrancaResumo cobranca,

    @NotNull
    @Schema(nullable = false, description = "Resumo do repasse do atendimento")
    RepasseResumo repasse,

    @NotNull
    @Schema(nullable = false, description = "Data de criacao do atendimento", example = "2024-03-10T15:33:42Z")
    LocalDateTime createdAt,

    @Nullable
    @Schema(nullable = true, description = "Data de atualizacao do atendimento", example = "2024-03-10T15:33:42Z")
    LocalDateTime updatedAt
) {
    public static AtendimentoIndividualResponse toDto(AtendimentoIndividualViewEntity atendimento) {
        return new AtendimentoIndividualResponse(
            atendimento.getId(),
            atendimento.getTipo(),
            atendimento.getDataHoraInicio(),
            atendimento.getDataHoraFim(),
            new AlunoResumo(atendimento.getAlunoId(), atendimento.getAlunoNome()),
            new ColaboradorResumo(atendimento.getColaboradorId(), atendimento.getColaboradorNome()),
            new CobrancaResumo(
                atendimento.getCobrancaId(),
                atendimento.getCobrancaValor(),
                atendimento.getCobrancaStatus(),
                atendimento.getCobrancaDataPagamento(),
                atendimento.getCobrancaFormaPagamento(),
                atendimento.getCobrancaComprovanteUrl(),
                atendimento.getCobrancaLoteId()
            ),
            new RepasseResumo(
                atendimento.getRepasseId(),
                atendimento.getRepasseValor(),
                atendimento.getRepasseStatus(),
                atendimento.getRepasseDataRepasse(),
                atendimento.getRepasseFormaPagamento(),
                atendimento.getRepasseComprovanteUrl(),
                atendimento.getRepasseLoteId()
            ),
            atendimento.getCreatedAt(),
            atendimento.getUpdatedAt()
        );
    }

    public record AlunoResumo(
        @NotNull
        @Schema(nullable = false)
        UUID id,

        @NotNull
        @Schema(nullable = false)
        String nome
    ) {
    }

    public record ColaboradorResumo(
        @NotNull
        @Schema(nullable = false)
        UUID id,

        @NotNull
        @Schema(nullable = false)
        String nome
    ) {
    }

    public record CobrancaResumo(
        @NotNull
        @Schema(nullable = false, description = "Identificador da cobrança")
        Long id,

        @NotNull
        @Schema(nullable = false)
        BigDecimal valor,

        @NotNull
        @Schema(nullable = false)
        String status,

        @Nullable
        @Schema(nullable = true)
        LocalDateTime dataPagamento,

        @Nullable
        @Schema(nullable = true)
        String formaPagamento,

        @Nullable
        @Schema(nullable = true)
        String comprovanteUrl,

        @Nullable
        @Schema(nullable = true)
        UUID loteId
    ) {
    }

    public record RepasseResumo(
        @NotNull
        @Schema(nullable = false, description = "Identificador do repasse")
        Long id,

        @NotNull
        @Schema(nullable = false)
        BigDecimal valor,

        @NotNull
        @Schema(nullable = false)
        String status,

        @Nullable
        @Schema(nullable = true)
        LocalDateTime dataRepasse,

        @Nullable
        @Schema(nullable = true)
        String formaPagamento,

        @Nullable
        @Schema(nullable = true)
        String comprovanteUrl,

        @Nullable
        @Schema(nullable = true)
        UUID loteId
    ) {
    }
}
