package aprimorar.atendimentos.individuais.web.dto.cobranca;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "Cobranças pagas que devem voltar para pendente")
public record CancelarCobrancasIndividualRequest(
    @NotEmpty(message = "Informe ao menos uma cobrança")
    List<@NotNull(message = "Informe o ID da cobrança") Long> cobrancaIds
) {}
