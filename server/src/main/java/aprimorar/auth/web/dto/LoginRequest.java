package aprimorar.auth.web.dto;

import aprimorar.common.utils.EmailUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "Informe o e-mail")
    @Email(message = "Informe um e-mail válido")
    @Schema(nullable = false, description = "E-mail do usuário")
    String email,

    @NotBlank(message = "Informe a senha")
    @Schema(nullable = false, description = "Senha do usuário", format = "password")
    String password
) {
    public LoginRequest {
        email = EmailUtils.normalize(email);
    }
}
