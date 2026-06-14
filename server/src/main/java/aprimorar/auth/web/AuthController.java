package aprimorar.auth.web;

import aprimorar.auth.web.dto.AuthRequestDTO;
import aprimorar.auth.web.dto.AuthResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import aprimorar.auth.application.AuthService;
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
    @SecurityRequirements({})
    @ApiResponse(responseCode = "200", description = "Usuario autenticado com sucesso.")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO request) {
        return ResponseEntity.ok(authService.authenticate(request.email(), request.password()));
    }
}
