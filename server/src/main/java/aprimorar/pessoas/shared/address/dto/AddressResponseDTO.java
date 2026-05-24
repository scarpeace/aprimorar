package aprimorar.pessoas.shared.address.dto;

import aprimorar.pessoas.shared.address.BrazilianStatesEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Endereço")
public record AddressResponseDTO(
        @NotNull String street,
        @NotNull String district,
        @NotNull String city,
        @NotNull BrazilianStatesEnum state,
        @NotNull String zip,
        @Schema(nullable = true)
        String complement
) {
}
