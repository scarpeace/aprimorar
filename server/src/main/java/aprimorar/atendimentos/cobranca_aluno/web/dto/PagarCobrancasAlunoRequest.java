package aprimorar.atendimentos.cobranca_aluno.web.dto;

import aprimorar.atendimentos.cobranca_aluno.domain.FormaPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(description = "Dados para pagamento de uma ou mais cobranças pendentes")
public record PagarCobrancasAlunoRequest(
    @NotEmpty(message = "Informe ao menos uma cobrança")
    List<@NotNull @Valid CobrancaAlunoItemRequest> cobrancas,

    @NotNull(message = "Informe a forma de pagamento")
    FormaPagamento formaPagamento,

    @Size(max = 500, message = "O comprovante não pode ter mais de 500 caracteres")
    String comprovanteUrl
) {}
