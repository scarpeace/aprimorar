package aprimorar.pessoas.colaborador.api.dto;

import aprimorar.pessoas.colaborador.internal.ColaboradorDutyEnum;
import aprimorar.pessoas.shared.address.dto.AddressResponseDTO;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record ColaboradorResponseDTO(
        @NotNull UUID id,
        @NotNull String name,
        @NotNull LocalDate birthdate,
        @NotNull String pix,
        @NotNull String contact,
        @NotNull String cpf,
        @NotNull String email,
        @NotNull ColaboradorDutyEnum duty,
        @Schema(implementation = AddressResponseDTO.class, description = "Endereço do funcionário")
        @NotNull AddressResponseDTO address,
        @Schema(nullable = true)
        @Nullable
        boolean active,
        @NotNull Instant createdAt,
        @Schema(nullable = true)
        @Nullable
        Instant updatedAt
) {}
