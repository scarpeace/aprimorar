package aprimorar.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import aprimorar.auth.domain.UserEntity;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private static final String SECRET = "12345678901234567890123456789012";

    private final JwtService jwtService = new JwtService(SECRET);

    @Test
    void shouldExtractUserIdFromValidToken() {
        UUID userId = UUID.randomUUID();
        UserEntity user = mock(UserEntity.class);
        when(user.getId()).thenReturn(userId);

        String token = jwtService.generateToken(user);

        assertThat(jwtService.validateAndExtractUserId(token)).contains(userId);
    }

    @Test
    void shouldRejectInvalidToken() {
        assertThat(jwtService.validateAndExtractUserId("token-invalido")).isEmpty();
    }
}
