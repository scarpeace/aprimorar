package aprimorar.financeiro.operacional.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status do lançamento financeiro")
public enum StatusDespesa {
    PENDENTE,
    PAGA,
    ATRASADA
}
