package aprimorar.despesas.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Forma de pagamento da despesa")
public enum FormaPagamento {
    PIX,
    DINHEIRO,
    CARTAO_CREDITO,
    CARTAO_DEBITO,
    BOLETO,
    TRANSFERENCIA
}
