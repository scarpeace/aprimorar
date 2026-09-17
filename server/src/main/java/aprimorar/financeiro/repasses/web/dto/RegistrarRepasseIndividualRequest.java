package aprimorar.financeiro.repasses.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

import aprimorar.financeiro.common.FormaPagamentoEnum;


@Schema(description = "Dados para registrar um ou mais repasses individuais")
public record RegistrarRepasseIndividualRequest(
    @NotEmpty(message = "Informe ao menos um repasse")
    List<@NotNull(message = "Informe o ID do repasse") Long> repasseIds,

    @NotNull(message = "Informe a forma de pagamento")
    FormaPagamentoEnum formaPagamento,

    @Size(max = 500, message = "O comprovante não pode ter mais de 500 caracteres")
    String comprovanteUrl
) {
}
