package aprimorar.instituicao.atendimentos_individuais.web.dto.atendimento;

import aprimorar.instituicao.atendimentos_individuais.domain.AtendimentoIndividual;
import aprimorar.instituicao.atendimentos_individuais.domain.enums.StatusAtendimentoIndividual;
import aprimorar.instituicao.atendimentos_individuais.domain.enums.TipoAtendimento;
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
    @Schema(nullable = false, description = "Status do atendimento", example = "AGENDADO")
    StatusAtendimentoIndividual status,

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
    public static AtendimentoIndividualResponse from(
        AtendimentoIndividual atendimento,
        aprimorar.financeiro.api.cobrancas.CobrancaResumo cobranca,
        aprimorar.financeiro.api.repasses.RepasseResumo repasse
    ) {
        return new AtendimentoIndividualResponse(
            atendimento.getId(),
            atendimento.getTipo(),
            atendimento.getStatus(),
            atendimento.getDataHoraInicio(),
            atendimento.getDataHoraFim(),
            new AlunoResumo(atendimento.getAluno().getId(), atendimento.getAluno().getNome()),
            new ColaboradorResumo(atendimento.getColaborador().getId(), atendimento.getColaborador().getNome()),
            new CobrancaResumo(
                cobranca.id(),
                cobranca.valor(),
                cobranca.status(),
                cobranca.dataPagamento(),
                cobranca.formaPagamento(),
                cobranca.comprovanteUrl(),
                cobranca.loteId()
            ),
            new RepasseResumo(
                repasse.id(),
                repasse.valor(),
                repasse.status(),
                repasse.dataRepasse(),
                repasse.formaPagamento(),
                repasse.comprovanteUrl(),
                repasse.loteId()
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
