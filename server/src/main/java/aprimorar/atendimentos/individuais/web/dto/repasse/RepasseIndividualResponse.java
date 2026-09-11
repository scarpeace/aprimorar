package aprimorar.atendimentos.individuais.web.dto.repasse;

import aprimorar.atendimentos.individuais.domain.AtendimentoIndividualViewEntity;
import aprimorar.atendimentos.individuais.domain.enums.FormaPagamento;
import aprimorar.atendimentos.individuais.domain.RepasseIndividualEntity;
import aprimorar.atendimentos.individuais.domain.enums.StatusRepasseIndividual;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Repasse individual")
public record RepasseIndividualResponse(
    Long id,
    Long atendimentoId,
    UUID colaboradorId,
    BigDecimal valor,
    StatusRepasseIndividual status,
    LocalDateTime dataRepasse,
    FormaPagamento formaPagamento,
    String comprovanteUrl
) {
    public static RepasseIndividualResponse toDto(RepasseIndividualEntity entity) {
        return new RepasseIndividualResponse(
            entity.getId(), entity.getAtendimentoId(), entity.getColaboradorId(), entity.getValor(),
            entity.getStatus(), entity.getDataRepasse(), entity.getFormaPagamento(), entity.getComprovanteUrl()
        );
    }

    public static RepasseIndividualResponse toDto(AtendimentoIndividualViewEntity view) {
        return new RepasseIndividualResponse(
            view.getRepasseId(), view.getId(), view.getColaboradorId(), view.getRepasseValor(),
            StatusRepasseIndividual.valueOf(view.getRepasseStatus()), view.getRepasseDataRepasse(),
            view.getRepasseFormaPagamento() == null ? null : FormaPagamento.valueOf(view.getRepasseFormaPagamento()),
            view.getRepasseComprovanteUrl()
        );
    }
}
