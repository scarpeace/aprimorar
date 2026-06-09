package aprimorar.pessoas.shared;

import aprimorar.shared.MapperUtils;
import jakarta.persistence.Embeddable;

@Embeddable
public class Email {

    private String value;

    protected Email() {}

    public Email(String value) {
        this.value = validate(value);
    }

    public String value() {
        return value;
    }

    private String validate(String value) {
        var normalized = MapperUtils.normalizeEmail(value);
        if (normalized == null || normalized.isBlank()) {
            throw new IllegalArgumentException("STATE: Email é obrigatório");
        }
        return normalized;
    }
}
