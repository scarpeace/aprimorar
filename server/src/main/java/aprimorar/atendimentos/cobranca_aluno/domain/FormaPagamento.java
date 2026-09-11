package aprimorar.atendimentos.cobranca_aluno.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Forma de pagamento do aluno")
public enum FormaPagamento {
    PIX,
    DINHEIRO,
    CARTAO_CREDITO,
    CARTAO_DEBITO,
    BOLETO,
    TRANSFERENCIA
}
