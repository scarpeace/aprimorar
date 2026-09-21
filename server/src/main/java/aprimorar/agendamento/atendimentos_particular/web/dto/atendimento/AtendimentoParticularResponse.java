package aprimorar.agendamento.atendimentos_particular.web.dto.atendimento;

import aprimorar.agendamento.atendimentos_particular.domain.AtendimentoParticular;
import aprimorar.agendamento.atendimentos_particular.domain.enums.StatusAtendimentoParticular;
import aprimorar.agendamento.atendimentos_particular.domain.enums.TipoAtendimentoParticular;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.api.CobrancaParticularSummary;
import aprimorar.financeiro.pagamentos_colaboradores.api.queries.RepasseQueryApi;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Dados do atendimento particular retornados pela API")
public record AtendimentoParticularResponse(
    @NotNull
    @Schema(nullable = false, description = "Identificador único do atendimento")
    Long id,

    @NotNull
    @Schema(nullable = false, description = "Tipo do atendimento")
    TipoAtendimentoParticular tipo,

    @NotNull
    @Schema(nullable = false, description = "Status do atendimento")
    StatusAtendimentoParticular status,

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

    public static AtendimentoParticularResponse toDto(
        AtendimentoParticular atendimento,
        CobrancaParticularSummary cobranca,
        RepasseQueryApi repasse
    ) {
        return new AtendimentoParticularResponse(
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
