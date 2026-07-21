package aprimorar.atendimentos.dto;

import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.enums.TipoAtendimento;
import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.domain.Colaborador;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Formato de payload para cadastro e atualização de atendimento")
public record AtendimentoRequest(
    @NotNull(message = "Informe o tipo do atendimento")
    @Schema(nullable = false, description = "Tipo de conteúdo do atendimento", example = "MENTORIA")
    TipoAtendimento tipo,

    @NotNull(message = "Informe a data de início")
    @Schema(nullable = false, description = "Data e hora de início do atendimento", example = "2026-11-20T14:00:00Z")
    LocalDateTime dataHoraInicio,

    @NotNull(message = "Informe a data de fim")
    @Schema(nullable = false, description = "Data e hora de fim do atendimento", example = "2026-11-20T15:30:00Z")
    LocalDateTime dataHoraFim,

    @NotNull(message = "Informe o valor")
    @PositiveOrZero(message = "O valor precisa ser maior ou igual a zero")
    @Schema(nullable = false, description = "Valor pago pelo aluno", example = "150.00")
    BigDecimal pagamentoAluno,

    @NotNull(message = "Informe o repasse")
    @PositiveOrZero(message = "O repasse precisa ser maior ou igual a zero")
    @Schema(nullable = false, description = "Valor de repasse ao colaborador", example = "100.00")
    BigDecimal repasseColaborador,

    @NotNull(message = "Informe o aluno")
    @Schema(nullable = false, description = "ID do aluno vinculado ao atendimento", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID alunoId,

    @NotNull(message = "Informe o colaborador")
    @Schema(nullable = false, description = "ID do colaborador vinculado ao atendimento", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID colaboradorId
) {
    public Atendimento toEntity(Aluno aluno, Colaborador colaborador) {
        return new Atendimento(
            dataHoraInicio,
            dataHoraFim,
            tipo,
            aluno,
            colaborador,
            pagamentoAluno,
            repasseColaborador
        );
    }
}
