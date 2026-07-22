package aprimorar.despesas.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Categoria da despesa operacional")
public enum CategoriaDespesa {
    CONTAS,
    PROFESSORES,
    FUNCIONARIOS,
    DESPENSA,
    MANUTENCAO,
    SERVICOS,
    ASSINATURAS
}
