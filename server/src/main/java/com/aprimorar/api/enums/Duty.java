package com.aprimorar.api.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Função/Papel do funcionário")
public enum Duty {
    TEACHER("Professor"),
    ADM("Administrativo"),
    THERAPIST("Terapeuta"),
    MENTOR("Mentor"),
    SYSTEM("Sistema");

    private final String description;

    Duty(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
