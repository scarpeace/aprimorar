package aprimorar.financeiro.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Categoria da despesa geral")
public enum CategoriaDespesa {
    CONTAS("Contas"),
    ADMINISTRATIVO("Administrativo"),
    DESPENSA("Despensa"),
    MANUTENCAO("Manutenção"),
    SERVICOS("Serviços"),
    MATERIAIS("Materiais"),
    ASSINATURAS("Assinaturas");

    private final String description;

    CategoriaDespesa(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
