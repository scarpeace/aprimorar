package aprimorar.auth.web.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import aprimorar.auth.domain.Role;
import aprimorar.auth.domain.User;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class UserResponseTest {

    @Test
    void deveMapearUsuarioParaRespostaUnificada() {
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        User user = new User("user@example.com", "hash", Role.SECRETARIA);
        user.deactivate();
        ReflectionTestUtils.setField(user, "id", userId);

        UserResponse response = UserResponse.from(user);

        assertEquals(userId, response.id());
        assertEquals("user@example.com", response.email());
        assertEquals(Role.SECRETARIA, response.role());
        assertFalse(response.enabled());
    }
}
