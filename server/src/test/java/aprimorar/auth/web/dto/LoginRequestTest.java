package aprimorar.auth.web.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LoginRequestTest {

    @Test
    void deveNormalizarEmailNaEntrada() {
        LoginRequest request = new LoginRequest("  User@Example.COM ", "senha");

        assertEquals("user@example.com", request.email());
        assertEquals("senha", request.password());
    }
}
