package aprimorar.auth.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.auth.domain.Role;
import aprimorar.auth.domain.User;
import aprimorar.auth.domain.exception.UserAlreadyExistsException;
import aprimorar.auth.domain.exception.UserNotFoundException;
import aprimorar.auth.infrastructure.UserRepository;
import aprimorar.auth.web.dto.UserCreateRequest;
import aprimorar.auth.web.dto.UserResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final UUID USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RefreshTokenService refreshTokenService;

    private UserService service;

    @BeforeEach
    void setUp() {
        service = new UserService(userRepository, passwordEncoder, refreshTokenService);
    }

    @Test
    void deveCriarUsuarioComSenhaCodificada() {
        User user = user(USER_ID, "user@example.com", Role.SECRETARIA);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("senha")).thenReturn("hash");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = service.create(
            new UserCreateRequest("user@example.com", "senha", Role.SECRETARIA)
        );

        assertEquals(USER_ID, response.id());
        assertEquals("user@example.com", response.email());
        assertEquals(Role.SECRETARIA, response.role());
        assertTrue(response.enabled());
    }

    @Test
    void naoDeveCriarUsuarioComEmailDuplicado() {
        when(userRepository.findByEmail("user@example.com"))
            .thenReturn(Optional.of(user(USER_ID, "user@example.com", Role.ADMIN)));

        assertThrows(
            UserAlreadyExistsException.class,
            () -> service.create(new UserCreateRequest("user@example.com", "senha", Role.ADMIN))
        );
    }

    @Test
    void deveListarUsuariosComoResponses() {
        User first = user(USER_ID, "first@example.com", Role.ADMIN);
        User second = user(
            UUID.fromString("22222222-2222-2222-2222-222222222222"),
            "second@example.com",
            Role.SECRETARIA
        );
        when(userRepository.findAll()).thenReturn(List.of(first, second));

        List<UserResponse> responses = service.findAll();

        assertEquals(2, responses.size());
        assertEquals("first@example.com", responses.get(0).email());
        assertEquals("second@example.com", responses.get(1).email());
    }

    @Test
    void deveDesativarUsuarioERevogarSeusTokens() {
        User user = user(USER_ID, "user@example.com", Role.ADMIN);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        service.deactivate(USER_ID);

        assertFalse(user.isEnabled());
        verify(refreshTokenService).revokeAll(USER_ID);
    }

    @Test
    void deveAtivarUsuario() {
        User user = user(USER_ID, "user@example.com", Role.ADMIN);
        user.deactivate();
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        service.activate(USER_ID);

        assertTrue(user.isEnabled());
    }

    @Test
    void deveExcluirUsuarioESeusTokens() {
        User user = user(USER_ID, "user@example.com", Role.ADMIN);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        service.delete(USER_ID);

        verify(refreshTokenService).deleteAll(USER_ID);
        verify(userRepository).delete(user);
    }

    @Test
    void deveFalharAoConsultarUsuarioInexistente() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.findById(USER_ID));
    }

    private static User user(UUID id, String email, Role role) {
        User user = new User(email, "hash", role);
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }
}
