package aprimorar.financeiro.despesas.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipo do lançamento financeiro")
public enum TipoDespesa {
    ENTRADA,
    SAIDA
}
