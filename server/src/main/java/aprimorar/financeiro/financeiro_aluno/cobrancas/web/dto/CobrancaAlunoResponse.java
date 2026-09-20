package aprimorar.financeiro.financeiro_aluno.cobrancas.web.dto;

import aprimorar.financeiro.api.financeiro_aluno.TipoOrigemCobrancaAluno;
import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.CobrancaAluno;
import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.enums.StatusCobrancaAluno;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Cobrança de aluno")
public record CobrancaAlunoResponse(
    @NotNull
    @Schema(nullable = false)
    Long id,

    @NotNull
    @Schema(nullable = false)
    Long origemId,

    @NotNull
    @Schema(nullable = false)
    TipoOrigemCobrancaAluno origemTipo,

    @NotNull
    @Schema(nullable = false)
    UUID alunoId,

    @NotNull
    @Schema(nullable = false)
    BigDecimal valorTotal,

    @NotNull
    @Schema(nullable = false)
    StatusCobrancaAluno status
) {
    public static CobrancaAlunoResponse toDto(CobrancaAluno entity) {
        return new CobrancaAlunoResponse(
            entity.getId(),
            entity.getOrigemId(),
            entity.getOrigemTipo(),
            entity.getAlunoId(),
            entity.getValorTotal(),
            entity.getStatus()
        );
    }
}
