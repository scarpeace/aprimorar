package aprimorar.pessoas.domain;

import aprimorar.shared.MapperUtils;
import jakarta.persistence.Embeddable;

@Embeddable
public class Cpf {

    private String value;

    protected Cpf() {}

    public Cpf(String value) {
        this.value = validate(value);
    }

    public String value() {
        return value;
    }

    private String validate(String value) {
        var normalized = MapperUtils.normalizeCpf(value);
        if (normalized == null || normalized.isBlank()) {
            throw new IllegalArgumentException("STATE: CPF do responsável é obrigatório");
        }
        return normalized;
    }
}
