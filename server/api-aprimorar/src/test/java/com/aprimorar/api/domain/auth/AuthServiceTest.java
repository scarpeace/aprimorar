package com.aprimorar.api.domain.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aprimorar.api.domain.auth.dto.AuthCurrentUserResponseDTO;
import com.aprimorar.api.domain.auth.repository.InternalUserRepository;
import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.enums.Duty;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final UUID INTERNAL_USER_ID = UUID.fromString("8ccdb801-d0af-4561-8d45-56d196350001");
    private static final UUID EMPLOYEE_ID = UUID.fromString("b71fa3e6-31f0-4ef5-a650-1bccae83302e");
    private static final Instant FIXED_INSTANT = Instant.parse("2026-04-18T12:00:00Z");
    private static final String USERNAME = "beatriz.santos";
    private static final String EMAIL = "beatriz.santos@731aprimorar.dev";
    private static final String PASSWORD = "admin123";
    private static final String PASSWORD_HASH = "$2a$10$V.e87EVXrIgVbF0W1h0P7es5X2xbYF9bBlt3F4iAfDdc0AGJi/7lu";

    @Mock
    private InternalUserRepository internalUserRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Clock applicationClock = Clock.fixed(FIXED_INSTANT, ZoneOffset.UTC);

    @InjectMocks
    private AuthService authService = new AuthService(internalUserRepository, passwordEncoder, applicationClock);

    @Nested
    @DisplayName("Login")
    class Login {

        @Test
        @DisplayName("should authenticate with username identifier")
        void shouldAuthenticateWithUsernameIdentifier() {
            InternalUser internalUser = activeInternalUser();

            when(internalUserRepository.findByUsernameOrEmployeeEmail(USERNAME)).thenReturn(Optional.of(internalUser));

            AuthCurrentUserResponseDTO response = authService.login(USERNAME, PASSWORD);

            assertThat(response.id()).isEqualTo(INTERNAL_USER_ID);
            assertThat(response.username()).isEqualTo(USERNAME);
            assertThat(response.email()).isEqualTo(EMAIL);
            assertThat(internalUser.getLastLoginAt()).isEqualTo(FIXED_INSTANT);
            verify(internalUserRepository).save(internalUser);
        }

        @Test
        @DisplayName("should authenticate with employee email identifier")
        void shouldAuthenticateWithEmployeeEmailIdentifier() {
            InternalUser internalUser = activeInternalUser();

            when(internalUserRepository.findByUsernameOrEmployeeEmail(EMAIL)).thenReturn(Optional.of(internalUser));

            AuthCurrentUserResponseDTO response = authService.login(EMAIL, PASSWORD);

            assertThat(response.id()).isEqualTo(INTERNAL_USER_ID);
            assertThat(response.username()).isEqualTo(USERNAME);
            assertThat(response.email()).isEqualTo(EMAIL);
            verify(internalUserRepository).save(internalUser);
        }

        @Test
        @DisplayName("should reject invalid password")
        void shouldRejectInvalidPassword() {
            when(internalUserRepository.findByUsernameOrEmployeeEmail(USERNAME)).thenReturn(Optional.of(activeInternalUser()));

            assertThatThrownBy(() -> authService.login(USERNAME, "senha-invalida"))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Credenciais inválidas");
        }

        @Test
        @DisplayName("should reject inactive internal user")
        void shouldRejectInactiveInternalUser() {
            when(internalUserRepository.findByUsernameOrEmployeeEmail(USERNAME)).thenReturn(Optional.of(inactiveInternalUser()));

            assertThatThrownBy(() -> authService.login(USERNAME, PASSWORD))
                .isInstanceOf(DisabledException.class)
                .hasMessage("Usuário interno inativo");

            verify(internalUserRepository).findByUsernameOrEmployeeEmail(USERNAME);
        }
    }

    @Nested
    @DisplayName("Current user")
    class CurrentUser {

        @Test
        @DisplayName("should map authenticated principal to current-user dto")
        void shouldMapAuthenticatedPrincipalToCurrentUserDto() {
            InternalUser internalUser = activeInternalUser();

            AuthCurrentUserResponseDTO response = authService.getCurrentUser(internalUser);

            assertThat(response)
                .extracting(
                    AuthCurrentUserResponseDTO::id,
                    AuthCurrentUserResponseDTO::username,
                    AuthCurrentUserResponseDTO::displayName,
                    AuthCurrentUserResponseDTO::email,
                    AuthCurrentUserResponseDTO::employeeId,
                    AuthCurrentUserResponseDTO::duty
                )
                .containsExactly(INTERNAL_USER_ID, USERNAME, "Beatriz Santos", EMAIL, EMPLOYEE_ID, Duty.ADM);
        }
    }

    private InternalUser activeInternalUser() {
        return internalUser(true);
    }

    private InternalUser inactiveInternalUser() {
        return internalUser(false);
    }

    private InternalUser internalUser(boolean active) {
        Employee employee = new Employee(
            "Beatriz Santos",
            LocalDate.of(2008, 12, 18),
            "33675240185",
            "61955228868",
            "89720151137",
            EMAIL,
            Duty.ADM
        );
        employee.setId(EMPLOYEE_ID);

        InternalUser internalUser = new InternalUser(employee, USERNAME, PASSWORD_HASH, active);
        internalUser.setId(INTERNAL_USER_ID);
        return internalUser;
    }
}
