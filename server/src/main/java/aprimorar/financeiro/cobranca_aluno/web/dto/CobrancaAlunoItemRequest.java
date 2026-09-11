package aprimorar.financeiro.cobranca_aluno.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Cobrança pendente a ser incluída em um pagamento")
public record CobrancaAlunoItemRequest(
    @NotNull(message = "Informe o ID da cobrança")
    Long cobrancaId
) {}
