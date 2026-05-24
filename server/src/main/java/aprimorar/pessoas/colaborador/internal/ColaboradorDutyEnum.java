package aprimorar.pessoas.colaborador.internal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Função/Papel do funcionário")
public enum ColaboradorDutyEnum {
    TEACHER("Professor"),
    ADM("Administrativo"),
    THERAPIST("Terapeuta"),
    MENTOR("Mentor"),
    SYSTEM("Sistema");

    private final String description;

    ColaboradorDutyEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
