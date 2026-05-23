package aprimorar.registration.employee.api.contract.dto;

import aprimorar.registration.employee.api.contract.DutyEnum;
import aprimorar.registration.shared.address.dto.AddressResponseDTO;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record EmployeeResponseDTO(
        @NotNull UUID id,
        @NotNull String name,
        @NotNull LocalDate birthdate,
        @NotNull String pix,
        @NotNull String contact,
        @NotNull String cpf,
        @NotNull String email,
        @NotNull DutyEnum duty,
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
