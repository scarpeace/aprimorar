package aprimorar.atendimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Resumo financeiro consolidado baseado em atendimentos e despesas gerais")
public record AtendimentosKpisDTO(
    @Schema(description = "Total cobrado dos alunos no periodo", example = "4200.00")
    BigDecimal totalStudentCharged,
    @Schema(description = "Total pendente de cobranca dos alunos no periodo", example = "850.00")
    BigDecimal totalStudentPending,
    @Schema(description = "Total pago aos colaboradores no periodo", example = "2800.00")
    BigDecimal totalEmployeePaid,
    @Schema(description = "Total pendente de pagamento aos colaboradores no periodo", example = "600.00")
    BigDecimal totalEmployeePending
) {}
