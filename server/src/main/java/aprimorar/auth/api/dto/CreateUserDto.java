package aprimorar.auth.api.dto;

import aprimorar.shared.enums.Role;

public record CreateUserDto(String username, String password, Role role) {
}
