package aprimorar.atendimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Resumo financeiro e quantitativo do aluno no periodo")
public record AlunoAtendimentosKpis(
    @Schema(description = "Total de atendimentos no periodo selecionado")
    Long totalAtendimentos,

    @Schema(description = "Total cobrado no periodo selecionado")
    BigDecimal totalCobrado,

    @Schema(description = "Total pendente no periodo selecionado")
    BigDecimal totalPendente
) {}
