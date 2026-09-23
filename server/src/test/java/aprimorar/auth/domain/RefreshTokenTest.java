package aprimorar.auth.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RefreshTokenTest {

    @Test
    void deveSerValidoAntesDaExpiracao() {
        RefreshToken token = token(Instant.now().plusSeconds(60));

        assertTrue(token.isValid());
    }

    @Test
    void deveSerInvalidoDepoisDaExpiracao() {
        RefreshToken token = token(Instant.now().minusSeconds(1));

        assertFalse(token.isValid());
    }

    @Test
    void deveSerInvalidadoAoSerRevogado() {
        RefreshToken token = token(Instant.now().plusSeconds(60));

        token.revoke();

        assertFalse(token.isValid());
    }

    private static RefreshToken token(Instant expiresAt) {
        return new RefreshToken(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "hash",
            expiresAt
        );
    }
}
