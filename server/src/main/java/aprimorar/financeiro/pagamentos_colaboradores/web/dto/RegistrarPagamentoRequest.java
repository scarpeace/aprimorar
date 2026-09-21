package aprimorar.financeiro.pagamentos_colaboradores.web.dto;

import aprimorar.common.FormaPagamentoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Dados para registrar o pagamento de repasses particulares")
public record RegistrarPagamentoRequest(
    @NotEmpty(message = "Informe ao menos um repasse")
    List<@NotNull(message = "Informe o ID do repasse") Long> repasseIds,

    @NotNull(message = "Informe a data do pagamento")
    @PastOrPresent(message = "A data do pagamento não pode ser futura")
    LocalDate dataPagamento,

    @NotNull(message = "Informe a forma de pagamento")
    FormaPagamentoEnum formaPagamento,

    @Size(max = 500, message = "O comprovante não pode ter mais de 500 caracteres")
    String comprovanteUrl
) {
}
