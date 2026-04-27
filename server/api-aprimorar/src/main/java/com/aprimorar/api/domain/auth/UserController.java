package com.aprimorar.api.domain.auth;

import com.aprimorar.api.domain.auth.dto.UserCreateRequestDTO;
import com.aprimorar.api.domain.auth.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/admin/users")
@Tag(name = "User Management")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(operationId = "listUsers", description = "Lista todos os usuários do sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso.")
    public ResponseEntity<List<UserResponseDTO>> list() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    @Operation(operationId = "createUser", description = "Cadastra um novo usuário para um funcionário.")
    @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso.")
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserCreateRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "deleteUser", description = "Remove o acesso de um usuário ao sistema.")
    @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso.")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
