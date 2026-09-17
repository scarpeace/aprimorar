package aprimorar.financeiro.common;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Forma de pagamento")
public enum FormaPagamentoEnum {
    PIX,
    DINHEIRO,
    CARTAO_CREDITO,
    CARTAO_DEBITO,
    BOLETO,
    TRANSFERENCIA
}
