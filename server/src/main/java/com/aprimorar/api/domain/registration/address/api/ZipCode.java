package com.aprimorar.api.domain.registration.address.api;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record ZipCode(
    @Column(name = "zip", nullable = false)
    String value
) {
    public ZipCode {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("CEP não pode ser vazio");
        }
        value = value.replaceAll("\\D", "");
        if (value.length() != 8) {
            throw new IllegalArgumentException("CEP deve conter 8 dígitos");
        }
    }

    public String format() {
        return value.substring(0, 5) + "-" + value.substring(5);
    }
}
