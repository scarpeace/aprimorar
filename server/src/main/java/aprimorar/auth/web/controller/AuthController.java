package aprimorar.auth.web.controller;

import aprimorar.auth.service.AuthService;
import aprimorar.auth.web.dto.AuthRequestDTO;
import aprimorar.auth.web.dto.AuthResponseDTO;
import aprimorar.auth.web.dto.UserRequestDTO;
import aprimorar.auth.web.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "Auth", description = "APIs de autenticação e usuários")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(operationId = "login", description = "Autentica um usuário e retorna um access token JWT.")
    @SecurityRequirements({})
    @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso.")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO request) {
        return ResponseEntity.ok(authService.authenticate(request.username(), request.password()));
    }

    @PostMapping("/users")
    @Operation(operationId = "createUser", summary = "Cria um novo usuário")
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO dto) {
        UserResponseDTO response = authService.createUser(dto);
        return ResponseEntity.created(URI.create("/v1/auth/users/" + response.id())).body(response);
    }

    @GetMapping("/users")
    @Operation(operationId = "listUsers", summary = "Lista todos os usuários")
    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    public ResponseEntity<List<UserResponseDTO>> listUsers() {
        return ResponseEntity.ok(authService.findAllUsers());
    }

    @GetMapping("/users/me/{username}")
    @Operation(operationId = "me", summary = "Retorna um usuário pelo e-mail")
    @ApiResponse(responseCode = "200", description = "Usuário retornado com sucesso")
    public ResponseEntity<UserResponseDTO> me(@PathVariable String username) {
        return ResponseEntity.ok(authService.findUserByUsername(username));
    }

    @DeleteMapping("/users/{id}")
    @Operation(operationId = "deleteUser", summary = "Exclui um usuário")
    @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        authService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{id}/deactivate")
    @Operation(operationId = "deactivateUser", summary = "Desativa um usuário")
    @ApiResponse(responseCode = "204", description = "Usuário desativado com sucesso")
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID id) {
        authService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{id}/activate")
    @Operation(operationId = "activateUser", summary = "Ativa um usuário")
    @ApiResponse(responseCode = "204", description = "Usuário ativado com sucesso")
    public ResponseEntity<Void> activateUser(@PathVariable UUID id) {
        authService.activateUser(id);
        return ResponseEntity.noContent().build();
    }
}
