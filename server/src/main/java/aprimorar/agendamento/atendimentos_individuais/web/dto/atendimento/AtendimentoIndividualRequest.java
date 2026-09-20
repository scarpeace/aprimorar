package aprimorar.agendamento.atendimentos_individuais.web.dto.atendimento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import aprimorar.agendamento.atendimentos_individuais.domain.enums.TipoAtendimento;

@Schema(description = "Formato de payload para cadastro e atualização de atendimento")
public record AtendimentoIndividualRequest(
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
    @Schema(nullable = false, description = "Valor da cobrança do aluno", example = "150.00")
    BigDecimal valorCobranca,

    @NotNull(message = "Informe o repasse")
    @PositiveOrZero(message = "O repasse precisa ser maior ou igual a zero")
    @Schema(nullable = false, description = "Valor do repasse ao colaborador", example = "100.00")
    BigDecimal valorRepasse,

    @NotNull(message = "Informe o aluno")
    @Schema(nullable = false, description = "ID do aluno vinculado ao atendimento", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID alunoId,

    @NotNull(message = "Informe o colaborador")
    @Schema(nullable = false, description = "ID do colaborador vinculado ao atendimento", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID colaboradorId
) {

    @AssertTrue(message = "O valor da cobrança não pode ser menor que o repasse")
    @Schema(hidden = true)
    public boolean valoresValidos() {
        if (valorCobranca == null || valorRepasse == null) {
            return true;
        }

        return valorCobranca.compareTo(valorRepasse) >= 0;
    }
}
