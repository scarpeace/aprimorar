package aprimorar.auth.web;

import aprimorar.auth.dto.UserCreateRequest;
import aprimorar.auth.dto.UserListResponse;
import aprimorar.auth.dto.UserResponse;
import aprimorar.auth.user.UserService;
import aprimorar.common.openapi.BadRequestProblemResponse;
import aprimorar.common.openapi.CommonProblemResponses;
import aprimorar.common.openapi.ConflictProblemResponse;
import aprimorar.common.openapi.NotFoundProblemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuário", description = "APIs administrativas de gestão de usuários")
@CommonProblemResponses
@PreAuthorize("hasRole('ADMIN')")
class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(operationId = "createUser", description = "Cria um novo usuário.")
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso.")
    @BadRequestProblemResponse
    @ConflictProblemResponse
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request) {
        UserResponse createdUser = userService.create(request);
        return ResponseEntity
            .created(URI.create("/users/" + createdUser.id()))
            .body(createdUser);
    }

    @GetMapping
    @Operation(operationId = "getUsers", description = "Lista todos os usuários.")
    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso.")
    public ResponseEntity<List<UserListResponse>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{userId}")
    @Operation(operationId = "getUserById", description = "Retorna um usuário por ID.")
    @ApiResponse(responseCode = "200", description = "Usuário retornado com sucesso.")
    @NotFoundProblemResponse
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @PatchMapping("/{userId}/deactivate")
    @Operation(operationId = "deactivateUser", description = "Desativa um usuário por ID.")
    @ApiResponse(responseCode = "204", description = "Usuário desativado com sucesso.")
    @NotFoundProblemResponse
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID userId) {
        userService.deactivate(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/activate")
    @Operation(operationId = "activateUser", description = "Ativa um usuário por ID.")
    @ApiResponse(responseCode = "204", description = "Usuário ativado com sucesso.")
    @NotFoundProblemResponse
    public ResponseEntity<Void> activateUser(@PathVariable UUID userId) {
        userService.activate(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    @Operation(operationId = "deleteUser", description = "Exclui um usuário por ID.")
    @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso.")
    @NotFoundProblemResponse
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
