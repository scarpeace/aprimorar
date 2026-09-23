package aprimorar.auth.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import aprimorar.auth.domain.Role;
import aprimorar.auth.domain.User;
import aprimorar.auth.domain.exception.AuthException;
import aprimorar.auth.infrastructure.JwtService;
import aprimorar.auth.infrastructure.UserRepository;
import aprimorar.auth.web.dto.AuthMeResponse;
import aprimorar.auth.web.dto.LoginRequest;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final UUID USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    private AuthService service;

    @BeforeEach
    void setUp() {
        service = new AuthService(
            authenticationManager,
            userRepository,
            jwtService,
            refreshTokenService,
            passwordEncoder
        );
    }

    @Test
    void deveAutenticarUsuarioEEmitirTokens() {
        User user = user(USER_ID, Role.ADMIN);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtService.generateAccessToken(user)).thenReturn("access-token");
        when(refreshTokenService.create(USER_ID)).thenReturn("refresh-token");

        LoginResult result = service.login(new LoginRequest("user@example.com", "senha"));

        assertEquals("access-token", result.accessToken());
        assertEquals("refresh-token", result.refreshToken());

        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
            ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(captor.capture());
        assertEquals("user@example.com", captor.getValue().getName());
        assertEquals("senha", captor.getValue().getCredentials());
    }

    @Test
    void deveTraduzirFalhaDeAutenticacaoParaAuthException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("invalid"));

        assertThrows(
            AuthException.class,
            () -> service.login(new LoginRequest("user@example.com", "senha"))
        );

        verifyNoInteractions(jwtService, refreshTokenService);
    }

    @Test
    void deveGerarNovoAccessTokenNoRefresh() {
        User user = user(USER_ID, Role.SECRETARIA);
        when(refreshTokenService.validate("refresh-token")).thenReturn(USER_ID);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(jwtService.generateAccessToken(user)).thenReturn("new-access-token");

        assertEquals("new-access-token", service.refresh("refresh-token"));
    }

    @Test
    void naoDeveRenovarTokenDeUsuarioInexistente() {
        when(refreshTokenService.validate("refresh-token")).thenReturn(USER_ID);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(AuthException.class, () -> service.refresh("refresh-token"));
    }

    @Test
    void deveRevogarRefreshTokenNoLogout() {
        service.logout("refresh-token");

        verify(refreshTokenService).revoke("refresh-token");
    }

    @Test
    void deveRetornarDadosDoUsuarioAtual() {
        User user = user(USER_ID, Role.ADMIN);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        AuthMeResponse response = service.me(USER_ID);

        assertEquals(USER_ID, response.id());
        assertEquals("user@example.com", response.email());
        assertEquals(Role.ADMIN, response.role());
    }

    @Test
    void deveCriarAdminQuandoAindaNaoExiste() {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("senha-segura")).thenReturn("hash");

        service.ensureAdmin("admin@example.com", "senha-segura");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("admin@example.com", captor.getValue().getEmail());
        assertEquals("hash", captor.getValue().getPassword());
        assertEquals(Role.ADMIN, captor.getValue().getRole());
    }

    private static User user(UUID id, Role role) {
        User user = new User("user@example.com", "hash", role);
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }
}
