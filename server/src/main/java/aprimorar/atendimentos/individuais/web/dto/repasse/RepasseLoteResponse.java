package aprimorar.atendimentos.individuais.web.dto.repasse;

import aprimorar.atendimentos.individuais.domain.enums.FormaPagamento;
import aprimorar.atendimentos.individuais.repository.repasse.RepasseLoteProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Lote de repasses pagos")
public record RepasseLoteResponse(
    UUID loteId,
    UUID colaboradorId,
    LocalDateTime dataRepasse,
    FormaPagamento formaPagamento,
    String comprovanteUrl,
    BigDecimal valorTotal,
    Long quantidadeRepasses
) {
    public static RepasseLoteResponse toDto(RepasseLoteProjection projection) {
        return new RepasseLoteResponse(
            projection.getLoteId(),
            projection.getColaboradorId(),
            projection.getDataRepasse(),
            projection.getFormaPagamento(),
            projection.getComprovanteUrl(),
            projection.getValorTotal(),
            projection.getQuantidadeRepasses()
        );
    }
}
