package aprimorar.atendimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Resumo financeiro e quantitativo do colaborador no periodo")
public record ColaboradorAtendimentosKpis(
    @Schema(description = "Total de atendimentos no periodo selecionado")
    Long totalAtendimentos,

    @Schema(description = "Total pago no periodo selecionado")
    BigDecimal totalPaid,

    @Schema(description = "Total em aberto no periodo selecionado")
    BigDecimal totalUnpaid
) {}
