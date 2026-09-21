package aprimorar.agendamento.atendimentos_individuais.web.dto;

import aprimorar.agendamento.atendimentos_individuais.domain.enums.TipoAtendimentoIndividual;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados para atualizar um atendimento individual")
public record AtualizarAtendimentoIndividualRequest(
    @NotNull(message = "Informe o tipo do atendimento")
    TipoAtendimentoIndividual tipo,

    @NotNull(message = "Informe a data de início")
    LocalDateTime dataHoraInicio,

    @NotNull(message = "Informe a data de fim")
    LocalDateTime dataHoraFim,

    @NotNull(message = "Informe o valor da cobrança")
    @PositiveOrZero(message = "O valor da cobrança não pode ser negativo")
    BigDecimal valorCobranca,

    @NotNull(message = "Informe o valor do repasse")
    @PositiveOrZero(message = "O valor do repasse não pode ser negativo")
    BigDecimal valorRepasse,

    @NotNull(message = "Informe o aluno")
    UUID alunoId,

    @NotNull(message = "Informe o colaborador")
    UUID colaboradorId
) {

    @AssertTrue(message = "O valor da cobrança não pode ser menor que o repasse")
    @Schema(hidden = true)
    public boolean valoresValidos() {
        return valorCobranca == null
            || valorRepasse == null
            || valorCobranca.compareTo(valorRepasse) >= 0;
    }
}
