package aprimorar.financeiro.pagamentos_colaboradores.api.commands;

import java.math.BigDecimal;
import java.util.UUID;

public record CriarRepasseCommandApi(
    Long atendimentoId,
    UUID colaboradorId,
    BigDecimal valor
) {

    public static boolean validate(CriarRepasseCommandApi command){
        return command.atendimentoId != null
            && command.colaboradorId != null
            && command.valor != null;
    }
}
