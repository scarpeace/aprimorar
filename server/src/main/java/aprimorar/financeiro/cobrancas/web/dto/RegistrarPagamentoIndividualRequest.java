package aprimorar.financeiro.cobrancas.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

import aprimorar.financeiro.common.FormaPagamentoEnum;


@Schema(description = "Dados para pagamento de uma ou mais cobranças individuais")
public record RegistrarPagamentoIndividualRequest(
    @NotEmpty(message = "Informe ao menos uma cobrança")
    List<@NotNull(message = "Informe o ID da cobrança") Long> cobrancaIds,

    @NotNull(message = "Informe a forma de pagamento")
    FormaPagamentoEnum formaPagamento,

    @Size(max = 500, message = "O comprovante não pode ter mais de 500 caracteres")
    String comprovanteUrl
) {
}
