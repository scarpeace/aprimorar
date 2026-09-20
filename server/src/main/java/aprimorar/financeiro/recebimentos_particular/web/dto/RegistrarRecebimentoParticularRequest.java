package aprimorar.financeiro.recebimentos_particular.web.dto;

import aprimorar.financeiro.common.FormaPagamentoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Dados para registrar o recebimento de cobranças particulares")
public record RegistrarRecebimentoParticularRequest(
    @NotEmpty(message = "Informe ao menos uma cobrança")
    List<@NotNull(message = "Informe o ID da cobrança") Long> cobrancaIds,

    @NotNull(message = "Informe a data do recebimento")
    @PastOrPresent(message = "A data do recebimento não pode ser futura")
    LocalDate dataRecebimento,

    @NotNull(message = "Informe a forma de pagamento")
    FormaPagamentoEnum formaPagamento,

    @Size(max = 500, message = "O comprovante não pode ter mais de 500 caracteres")
    String comprovanteUrl
) {
    @AssertTrue(message = "Não informe a mesma cobrança mais de uma vez")
    @Schema(hidden = true)
    public boolean idsValidos() {
        if (cobrancaIds == null) {
            return true;
        }

        return cobrancaIds.size() == cobrancaIds.stream().distinct().count();
    }
}
