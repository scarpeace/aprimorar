package aprimorar.auth.dto;

import aprimorar.auth.user.Role;
import java.util.UUID;

public record AuthMeResponse(UUID id, String email, Role role) {
}
