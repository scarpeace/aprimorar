package aprimorar.auth.dto;

import aprimorar.auth.user.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para criação de um usuário")
public record UserCreateRequest(
    @NotBlank(message = "O e-mail do usuário é obrigatório")
    @Email(message = "Use um e-mail válido")
    @Schema(nullable = false, description = "E-mail do usuário", example = "secretaria@aprimorar.com")
    String email,

    @NotBlank(message = "A senha do usuário é obrigatória")
    @Schema(nullable = false, description = "Senha do usuário", format = "password", example = "senha-segura")
    String password,

    @NotNull(message = "A role do usuário é obrigatória")
    @Schema(nullable = false, description = "Perfil de acesso do usuário", example = "SECRETARIA")
    Role role
) {
}
