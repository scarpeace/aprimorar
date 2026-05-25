package aprimorar.pessoas.responsavel.api.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Formato de payload para criar um novo responsável")
public record ResponsavelRequestDTO(
        @NotBlank(message = "Nome do responsável é obrigatório")
        @Schema(nullable = false, description = "Nome do responsável", example = "João Silva")
        String name,

        @NotBlank(message = "Email do responsável é obrigatório")
        @Email(message = "Email deve ser um endereço de email válido")
        @Schema(nullable = false, description = "Email do responsável", example = "[EMAIL_ADDRESS]")
        String email,

        @NotBlank(message = "Contato do responsável é obrigatório")
        @Schema(nullable = false, description = "Contato do responsável", example = "11999999999")
        String contact,

        @Schema(nullable = true, description = "Data de nascimento do responsável", example = "1990-01-01")
        LocalDate birthdate,

        @Schema(nullable = true, description = "Pix do responsável", example = "pix@pix.com")
        String pix,

        @NotBlank(message = "CPF do responsável é obrigatório")
        @Schema(nullable = false, description = "CPF do responsável", example = "12345678901")
        String cpf
) {
}
