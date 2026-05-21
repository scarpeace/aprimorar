package aprimorar.auth.internal;

import aprimorar.auth.api.dto.AuthRequestDTO;
import aprimorar.auth.api.dto.AuthResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "Auth", description = "Authentication APIs")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(operationId = "login", description = "Autentica um usuario e retorna um access token JWT.")
    @ApiResponse(responseCode = "200", description = "Usuario autenticado com sucesso.")
    @ApiResponse(responseCode = "401", description = "Credenciais invalidas.")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO request) {
        return ResponseEntity.ok(authService.authenticate(request.email(), request.password()));
    }
}
