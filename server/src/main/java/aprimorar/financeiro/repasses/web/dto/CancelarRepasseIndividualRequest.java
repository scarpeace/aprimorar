package aprimorar.financeiro.repasses.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "Repasses pagos que devem voltar para pendente")
public record CancelarRepasseIndividualRequest(
    @NotEmpty(message = "Informe ao menos um repasse")
    List<@NotNull(message = "Informe o ID do repasse") Long> repasseIds
) {
}
