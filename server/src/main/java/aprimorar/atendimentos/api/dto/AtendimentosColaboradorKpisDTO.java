package aprimorar.atendimentos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Resumo financeiro de colaborador no periodo")
public record AtendimentosColaboradorKpisDTO(
    @Schema(description = "ID do colaborador", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID employeeId,
    @Schema(description = "Total de atendimentos no periodo", example = "12")
    long totalAtendimentos,
    @Schema(description = "Total pago ao colaborador no periodo", example = "3200.00")
    BigDecimal totalPaid,
    @Schema(description = "Total pendente de pagamento ao colaborador no periodo", example = "450.00")
    BigDecimal totalPending
) {
}
