package aprimorar.auth.internal;

import aprimorar.auth.api.dto.UserRequestDTO;
import aprimorar.auth.api.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("/v1/users")
@Tag(name = "User", description = "User management endpoints")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(operationId = "createUser", summary = "Create a new user")
    @ApiResponse(responseCode = "201", description = "Usuario criado com sucesso")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO dto) {
        var response = userService.createUser(dto);
        return ResponseEntity.created(URI.create("/v1/users/" + response.id())).body(response);
    }

    @GetMapping
    @Operation(operationId = "listUsers", summary = "List all users")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios retornada com sucesso")
    public ResponseEntity<List<UserResponseDTO>> listUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "deleteUser", summary = "Delete a user")
    @ApiResponse(responseCode = "204", description = "Usuario excluido com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/archive")
    @Operation(operationId = "archiveUser", summary = "Toggle active status of a user")
    @ApiResponse(responseCode = "200", description = "Status ativo alternado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
    public ResponseEntity<UserResponseDTO> archiveUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.toggleActive(id));
    }
}
