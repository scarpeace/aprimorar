package aprimorar.pessoas.colaborador.internal.dto;

import java.time.LocalDate;

import aprimorar.pessoas.colaborador.internal.ColaboradorDutyEnum;
import aprimorar.pessoas.shared.address.dto.AddressRequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

@Schema(description = "Formato de payload para criar um novo colaborador")
public record ColaboradorRequestDTO(
        @NotBlank(message = "Nome do funcionário é obrigatório")
        @Schema(description = "Nome completo do colaborador", example = "Joao Pereira")
        String name,
        @NotNull(message = "A data de nascimento do funcionário é obrigatória")
        @Past(message = "A data de nascimento do funcionário deve estar no passado")
        @Schema(description = "Data de nascimento do colaborador", example = "1990-05-21")
        LocalDate birthdate,
        @NotBlank(message = "Chave PIX do funcionário é obrigatória")
        @Schema(description = "Chave PIX do colaborador", example = "joao@pix.com")
        String pix,
        @NotBlank(message = "Contato do funcionário é obrigatório")
        @Schema(description = "Telefone de contato do colaborador", example = "(61) 99999-9999")
        String contact,
        @NotBlank(message = "CPF do funcionário é obrigatório")
        @Schema(description = "CPF do colaborador", example = "123.456.789-00")
        String cpf,
        @NotBlank(message = "Email do funcionário é obrigatório")
        @Email(message = "Email deve ser um endereço de email válido")
        @Schema(description = "Email do colaborador", example = "joao@empresa.com")
        String email,
        @NotNull(message = "Papel do funcionário é obrigatório")
        @Schema(description = "Função do colaborador", example = "TEACHER")
        ColaboradorDutyEnum duty,
        @Valid
        @NotNull(message = "Endereço do funcionário é obrigatório")
        @Schema(nullable = false, description = "Endereço do colaborador", implementation = AddressRequestDTO.class)
        AddressRequestDTO address) {
}
