package aprimorar.registration.employee.api.dto;

import java.time.LocalDate;

import aprimorar.registration.shared.address.dto.AddressRequestDTO;
import aprimorar.registration.shared.enums.Duty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

@Schema(description = "Formato de payload para criar um novo colaborador")
public record EmployeeRequestDTO(
        @NotBlank(message = "Nome do funcionário é obrigatório")
        String name,
        @NotNull(message = "A data de nascimento do funcionário é obrigatória")
        @Past(message = "A data de nascimento do funcionário deve estar no passado")
        LocalDate birthdate,
        @NotBlank(message = "Chave PIX do funcionário é obrigatória")
        String pix,
        @NotBlank(message = "Contato do funcionário é obrigatório")
        String contact,
        @NotBlank(message = "CPF do funcionário é obrigatório")
        String cpf,
        @NotBlank(message = "Email do funcionário é obrigatório")
        @Email(message = "Email deve ser um endereço de email válido")
        String email,
        @NotNull(message = "Papel do funcionário é obrigatório")
        Duty duty,
        @Valid
        @NotNull(message = "Endereço do funcionário é obrigatório")
        @Schema(nullable = false, description = "Endereço do funcionário", implementation = AddressRequestDTO.class)
        AddressRequestDTO address) {
}
