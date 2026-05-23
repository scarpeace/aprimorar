package aprimorar.pessoas.colaborador.api.contract;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Função/Papel do funcionário")
public enum DutyEnum {
    TEACHER("Professor"),
    ADM("Administrativo"),
    THERAPIST("Terapeuta"),
    MENTOR("Mentor"),
    SYSTEM("Sistema");

    private final String description;

    DutyEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
