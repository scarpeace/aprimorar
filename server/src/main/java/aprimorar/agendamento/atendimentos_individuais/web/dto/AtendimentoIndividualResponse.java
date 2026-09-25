package aprimorar.agendamento.atendimentos_individuais.web.dto;

import aprimorar.agendamento.atendimentos_individuais.domain.AtendimentoIndividual;
import aprimorar.agendamento.atendimentos_individuais.domain.enums.StatusAtendimentoIndividual;
import aprimorar.agendamento.atendimentos_individuais.domain.enums.TipoAtendimentoIndividual;
import aprimorar.financeiro.recebimentos_alunos.api.queries.CobrancaSummary;
import aprimorar.financeiro.repasses_colaboradores.api.queries.RepasseSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados do atendimento individual retornados pela API")
public record AtendimentoIndividualResponse(
    @NotNull
    @Schema(nullable = false, description = "Identificador único do atendimento")
    Long id,

    @NotNull
    @Schema(nullable = false, description = "Tipo do atendimento")
    TipoAtendimentoIndividual tipo,

    @NotNull
    @Schema(nullable = false, description = "Status do atendimento")
    StatusAtendimentoIndividual status,

    @NotNull
    @Schema(nullable = false, description = "Data e hora de início")
    LocalDateTime dataHoraInicio,

    @NotNull
    @Schema(nullable = false, description = "Data e hora de fim")
    LocalDateTime dataHoraFim,

    @NotNull
    @Schema(nullable = false, description = "Identificador do aluno vinculado")
    UUID alunoId,

    @NotNull
    @Schema(nullable = false, description = "Nome do aluno vinculado")
    String alunoNome,

    @NotNull
    @Schema(nullable = false, description = "Identificador do colaborador vinculado")
    UUID colaboradorId,

    @NotNull
    @Schema(nullable = false, description = "Nome do colaborador vinculado")
    String colaboradorNome,

    @NotNull
    @Schema(nullable = false, description = "Valor da cobrança vinculada")
    BigDecimal valorCobranca,

    @NotNull
    @Schema(nullable = false, description = "Status da cobrança do atendimento")
    String cobrancaStatus,

    @Nullable
    @Schema(nullable = true, description = "Identificador do recebimento associado à cobrança")
    UUID recebimentoId,

    @NotNull
    @Schema(nullable = false, description = "Valor do repasse vinculado")
    BigDecimal valorRepasse,

    @NotNull
    @Schema(nullable = false, description = "Status do repasse do atendimento")
    String repasseStatus,

    @Nullable
    @Schema(nullable = true, description = "Identificador do pagamento associado ao repasse")
    UUID pagamentoId,

    @NotNull
    @Schema(nullable = false, description = "Data de criação do atendimento")
    LocalDateTime createdAt,

    @Nullable
    @Schema(nullable = true, description = "Data de atualização do atendimento")
    LocalDateTime updatedAt
) {

    public static AtendimentoIndividualResponse toDto(
        AtendimentoIndividual atendimento,
        CobrancaSummary cobranca,
        RepasseSummary repasse
    ) {
        return new AtendimentoIndividualResponse(
            atendimento.getId(),
            atendimento.getTipo(),
            atendimento.getStatus(),
            atendimento.getDataHoraInicio(),
            atendimento.getDataHoraFim(),
            atendimento.getAluno().getId(),
            atendimento.getAluno().getNome(),
            atendimento.getColaborador().getId(),
            atendimento.getColaborador().getNome(),
            cobranca.valor(),
            cobranca.status(),
            cobranca.recebimentoId(),
            repasse.valor(),
            repasse.status(),
            repasse.pagamentoId(),
            atendimento.getCreatedAt(),
            atendimento.getUpdatedAt()
        );
    }
}
