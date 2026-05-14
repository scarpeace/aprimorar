package aprimorar.expense.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Categoria da despesa geral")
public enum ExpenseCategory {
    CONTAS("Contas"),
    ADMINISTRATIVO("Administrativo"),
    DESPENSA("Despensa"),
    MANUTENCAO("Manutenção"),
    SERVICOS("Serviços"),
    MATERIAIS("Materiais"),
    ASSINATURAS("Assinaturas");

    private final String description;

    ExpenseCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
