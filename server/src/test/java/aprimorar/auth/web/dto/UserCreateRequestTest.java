package aprimorar.auth.web.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aprimorar.auth.domain.Role;
import org.junit.jupiter.api.Test;

class UserCreateRequestTest {

    @Test
    void deveNormalizarEmailNaEntrada() {
        UserCreateRequest request = new UserCreateRequest(
            "  Secretaria@Example.COM ",
            "senha",
            Role.SECRETARIA
        );

        assertEquals("secretaria@example.com", request.email());
        assertEquals("senha", request.password());
        assertEquals(Role.SECRETARIA, request.role());
    }
}
