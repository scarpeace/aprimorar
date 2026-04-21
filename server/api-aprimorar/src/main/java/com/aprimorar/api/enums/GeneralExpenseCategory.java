package com.aprimorar.api.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Categoria de despesa geral")
public enum GeneralExpenseCategory {
    CONTAS("Contas"),
    ADMINISTRATIVO("Administrativo"),
    DESPENSA("Despensa"),
    MANUTENCAO("Manutenção"),
    SERVICOS("Serviços"),
    MATERIAIS("Materiais");

    private final String description;

    GeneralExpenseCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
