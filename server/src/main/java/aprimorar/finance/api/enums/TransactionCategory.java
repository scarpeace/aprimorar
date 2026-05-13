package aprimorar.finance.api.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Categoria da movimentação financeira")
public enum TransactionCategory {
    COBRANCA_ALUNO("Cobrança do aluno"),
    PAGAMENTO_COLABORADOR("Pagamento do colaborador"),
    CONTAS("Contas"),
    ADMINISTRATIVO("Administrativo"),
    DESPENSA("Despensa"),
    MANUTENCAO("Manutenção"),
    SERVICOS("Serviços"),
    MATERIAIS("Materiais"),
    ASSINATURAS("Assinaturas");

    private final String description;

    TransactionCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
