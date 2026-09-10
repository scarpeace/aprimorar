package aprimorar.financeiro.despesas.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Categoria do lançamento financeiro")
public enum CategoriaDespesa {
    CONTAS,
    PROFESSORES,
    FUNCIONARIOS,
    DESPENSA,
    MANUTENCAO,
    SERVICOS,
    ASSINATURAS
}
