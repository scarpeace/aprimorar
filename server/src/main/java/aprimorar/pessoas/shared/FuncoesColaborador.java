package aprimorar.pessoas.shared;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Função/Papel do funcionário")
public enum FuncoesColaborador {
    PROFESSOR("Professor"),
    ADMINISTRATIVO("Administrativo"),
    TERAPEUTA("Terapeuta"),
    MENTOR("Mentor"),
    SISTEMA("Sistema");

    private final String description;

    FuncoesColaborador(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
