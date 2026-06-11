package aprimorar.atendimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Resumo financeiro de aluno no periodo")
public record AtendimentosAlunosKpisDTO(
    @Schema(description = "ID do aluno", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID alunoId,
    @Schema(description = "Total de atendimentos no periodo", example = "12")
    long totalAtendimentos,
    @Schema(description = "Total cobrado do aluno no periodo", example = "3200.00")
    BigDecimal totalCobrado,
    @Schema(description = "Total pendente de cobranca do aluno no periodo", example = "450.00")
    BigDecimal totalPendente
) {
}
