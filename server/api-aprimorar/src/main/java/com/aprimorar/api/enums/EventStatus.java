package com.aprimorar.api.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status do evento")
public enum EventStatus {
    SCHEDULED("Agendado"),
    COMPLETED("Concluído"),
    CANCELED("Cancelado");

    private final String description;

    EventStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
