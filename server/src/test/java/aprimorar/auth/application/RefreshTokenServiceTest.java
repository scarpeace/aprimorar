package aprimorar.auth.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.auth.domain.RefreshToken;
import aprimorar.auth.domain.exception.AuthException;
import aprimorar.auth.infrastructure.RefreshTokenRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    private static final UUID USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Mock
    private RefreshTokenRepository repository;

    private RefreshTokenService service;

    @BeforeEach
    void setUp() {
        service = new RefreshTokenService(repository, 30);
    }

    @Test
    void deveCriarRefreshTokenPersistindoApenasSeuHash() {
        when(repository.save(any(RefreshToken.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        String rawToken = service.create(USER_ID);

        assertNotNull(rawToken);
        assertTrue(rawToken.length() > 20);

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(repository).save(captor.capture());
        assertEquals(USER_ID, captor.getValue().getUserId());
        assertTrue(captor.getValue().isValid());
    }

    @Test
    void deveValidarRefreshTokenAtivo() {
        RefreshToken token = token(USER_ID, Instant.now().plusSeconds(60));
        when(repository.findByTokenHash(any(String.class))).thenReturn(Optional.of(token));

        assertEquals(USER_ID, service.validate("raw-token"));
    }

    @Test
    void naoDeveValidarRefreshTokenInexistente() {
        when(repository.findByTokenHash(any(String.class))).thenReturn(Optional.empty());

        assertThrows(AuthException.class, () -> service.validate("raw-token"));
    }

    @Test
    void naoDeveValidarRefreshTokenExpirado() {
        RefreshToken token = token(USER_ID, Instant.now().minusSeconds(1));
        when(repository.findByTokenHash(any(String.class))).thenReturn(Optional.of(token));

        assertThrows(AuthException.class, () -> service.validate("raw-token"));
    }

    @Test
    void deveRevogarRefreshTokenEncontrado() {
        RefreshToken token = token(USER_ID, Instant.now().plusSeconds(60));
        when(repository.findByTokenHash(any(String.class))).thenReturn(Optional.of(token));

        service.revoke("raw-token");

        assertFalse(token.isValid());
    }

    @Test
    void deveRevogarTodosOsTokensDoUsuario() {
        RefreshToken first = token(USER_ID, Instant.now().plusSeconds(60));
        RefreshToken second = token(USER_ID, Instant.now().plusSeconds(60));
        when(repository.findAllByUserId(USER_ID)).thenReturn(List.of(first, second));

        service.revokeAll(USER_ID);

        assertFalse(first.isValid());
        assertFalse(second.isValid());
    }

    @Test
    void deveExcluirTodosOsTokensDoUsuario() {
        service.deleteAll(USER_ID);

        verify(repository).deleteAllByUserId(USER_ID);
    }

    @Test
    void deveRetornarQuantidadeDeTokensInativosRemovidos() {
        when(repository.deleteInactive(any(Instant.class))).thenReturn(3);

        assertEquals(3, service.deleteInactive());
    }

    private static RefreshToken token(UUID userId, Instant expiresAt) {
        return new RefreshToken(
            UUID.randomUUID(),
            userId,
            "hash",
            expiresAt
        );
    }
}
