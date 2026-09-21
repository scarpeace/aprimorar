package aprimorar.financeiro.recebimentos_alunos.api.commands;

import java.math.BigDecimal;
import java.util.UUID;

public record AtualizarCobrancaCommandApi(
    Long atendimentoId,
    UUID alunoId,
    BigDecimal valor
) {

    public static boolean validate(AtualizarCobrancaCommandApi command) {
        return command != null
            && command.atendimentoId != null
            && command.alunoId != null
            && command.valor != null;
    }
}
