package aprimorar.auth.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void deveCriarUsuarioHabilitadoComAutoridadeDaRole() {
        User user = new User("admin@example.com", "hash", Role.ADMIN);

        assertEquals("admin@example.com", user.getEmail());
        assertEquals("hash", user.getPassword());
        assertEquals("admin@example.com", user.getUsername());
        assertEquals(Role.ADMIN, user.getRole());
        assertTrue(user.isEnabled());
        assertEquals("ROLE_ADMIN", user.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void deveAlternarEstadoDeHabilitacao() {
        User user = new User("user@example.com", "hash", Role.SECRETARIA);

        user.deactivate();
        assertFalse(user.isEnabled());

        user.activate();
        assertTrue(user.isEnabled());
    }

    @Test
    void naoDeveAceitarDadosObrigatoriosNulos() {
        assertThrows(NullPointerException.class, () -> new User(null, "hash", Role.ADMIN));
        assertThrows(NullPointerException.class, () -> new User("user@example.com", null, Role.ADMIN));
        assertThrows(NullPointerException.class, () -> new User("user@example.com", "hash", null));
    }
}
