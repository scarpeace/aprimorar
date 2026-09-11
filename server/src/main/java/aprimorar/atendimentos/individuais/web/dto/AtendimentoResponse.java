package aprimorar.atendimentos.individuais.web.dto;

import aprimorar.atendimentos.individuais.domain.AtendimentoConsultaViewEntity;
import aprimorar.atendimentos.individuais.enums.TipoAtendimento;
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
    @Schema(nullable = false, description = "Valor de repasse ao colaborador", example = "100.00")
    BigDecimal repasseColaborador,

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
    @Schema(nullable = false, description = "Data de criacao do atendimento", example = "2024-03-10T15:33:42Z")
    LocalDateTime createdAt,

    @Nullable
    @Schema(nullable = true, description = "Data de atualizacao do atendimento", example = "2024-03-10T15:33:42Z")
    LocalDateTime updatedAt
) {
    public static AtendimentoResponse toDto(AtendimentoConsultaViewEntity atendimento) {
        return new AtendimentoResponse(
            atendimento.getId(),
            atendimento.getTipo(),
            atendimento.getDataHoraInicio(),
            atendimento.getDataHoraFim(),
            atendimento.getRepasseColaborador(),
            new AlunoResumo(atendimento.getAlunoId(), atendimento.getAlunoNome()),
            new ColaboradorResumo(atendimento.getColaboradorId(), atendimento.getColaboradorNome()),
            new CobrancaResumo(
                atendimento.getCobrancaId(),
                atendimento.getCobrancaValor(),
                atendimento.getCobrancaStatus(),
                atendimento.getCobrancaDataPagamento(),
                atendimento.getCobrancaFormaPagamento(),
                atendimento.getCobrancaComprovanteUrl()
            ),
            atendimento.getCreatedAt(),
            atendimento.getUpdatedAt()
        );
    }

    public record AlunoResumo(UUID id, String nome) {
    }

    public record ColaboradorResumo(UUID id, String nome) {
    }

    public record CobrancaResumo(
        Long id,
        BigDecimal valor,
        String status,
        LocalDateTime dataPagamento,
        String formaPagamento,
        String comprovanteUrl
    ) {
    }
}
