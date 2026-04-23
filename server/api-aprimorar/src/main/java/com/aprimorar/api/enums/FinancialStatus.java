package com.aprimorar.api.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status financeiro")
public enum FinancialStatus {
    PENDING("Pendente"),
    PAID("Pago");

    private final String description;

    FinancialStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
