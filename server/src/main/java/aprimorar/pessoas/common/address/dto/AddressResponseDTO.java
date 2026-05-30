package aprimorar.pessoas.common.address.dto;

import aprimorar.pessoas.common.address.BrazilianStatesEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Endereço")
public record AddressResponseDTO(
        @NotNull
        @Schema(description = "Logradouro", example = "SQS 406 Bloco C")
        String street,
        @NotNull
        @Schema(description = "Bairro", example = "Asa Sul")
        String district,
        @NotNull
        @Schema(description = "Cidade", example = "Brasília")
        String city,
        @NotNull
        @Schema(description = "Estado", example = "DF")
        BrazilianStatesEnum state,
        @NotNull
        @Schema(description = "CEP", example = "70254-010")
        String zip,
        @Schema(nullable = true, description = "Complemento do endereço", example = "Apto 101")
        String complement
) {
}
