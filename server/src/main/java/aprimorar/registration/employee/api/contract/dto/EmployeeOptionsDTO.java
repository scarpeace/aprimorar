package aprimorar.registration.employee.api.contract.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Formato de payload para obter opções de colaboradores")
public record EmployeeOptionsDTO(
        @Schema(description = "ID do funcionário")
        UUID id,
        @Schema(description = "Nome do funcionário")
        String name
) {
}
