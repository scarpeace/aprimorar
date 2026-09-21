package aprimorar.agendamento.atendimentos_individuais.web.dto;

import aprimorar.agendamento.atendimentos_individuais.domain.AtendimentoIndividual;
import aprimorar.agendamento.atendimentos_individuais.domain.enums.StatusAtendimentoIndividual;
import aprimorar.agendamento.atendimentos_individuais.domain.enums.TipoAtendimentoIndividual;
import aprimorar.financeiro.recebimentos_alunos.api.queries.CobrancaSummary;
import aprimorar.financeiro.repasses_colaboradores.api.queries.RepasseSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    @Schema(nullable = false, description = "Nome do aluno vinculado")
    String alunoNome,

    @NotNull
    @Schema(nullable = false, description = "Nome do colaborador vinculado")
    String colaboradorNome,

    @NotNull
    @Schema(nullable = false, description = "Status da cobrança do atendimento")
    String cobrancaStatus,

    @NotNull
    @Schema(nullable = false, description = "Status do repasse do atendimento")
    String repasseStatus,

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
            atendimento.getAluno().getNome(),
            atendimento.getColaborador().getNome(),
            cobranca.status(),
            repasse.status(),
            atendimento.getCreatedAt(),
            atendimento.getUpdatedAt()
        );
    }
}
