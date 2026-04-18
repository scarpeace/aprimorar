package com.aprimorar.api.domain.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aprimorar.api.domain.auth.dto.AuthCurrentUserResponseDTO;
import com.aprimorar.api.domain.auth.repository.StaffAccountRepository;
import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.enums.Duty;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final UUID STAFF_ACCOUNT_ID = UUID.fromString("8ccdb801-d0af-4561-8d45-56d196350001");
    private static final UUID EMPLOYEE_ID = UUID.fromString("b71fa3e6-31f0-4ef5-a650-1bccae83302e");
    private static final Instant FIXED_INSTANT = Instant.parse("2026-04-18T12:00:00Z");
    private static final String USERNAME = "beatriz.santos";
    private static final String EMAIL = "beatriz.santos@731aprimorar.dev";
    private static final String PASSWORD = "admin123";
    @Mock
    private StaffAccountRepository staffAccountRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Clock applicationClock = Clock.fixed(FIXED_INSTANT, ZoneOffset.UTC);

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(staffAccountRepository, passwordEncoder, applicationClock);
    }

    @Nested
    @DisplayName("Login")
    class Login {

        @Test
        @DisplayName("should authenticate with username identifier")
        void shouldAuthenticateWithUsernameIdentifier() {
            StaffAccount staffAccount = activeStaffAccount();

            when(staffAccountRepository.findByUsernameOrEmployeeEmail(USERNAME)).thenReturn(Optional.of(staffAccount));

            AuthCurrentUserResponseDTO response = authService.login(USERNAME, PASSWORD);

            assertThat(response.id()).isEqualTo(STAFF_ACCOUNT_ID);
            assertThat(response.username()).isEqualTo(USERNAME);
            assertThat(response.email()).isEqualTo(EMAIL);
            assertThat(staffAccount.getLastLoginAt()).isEqualTo(FIXED_INSTANT);
            verify(staffAccountRepository).save(staffAccount);
        }

        @Test
        @DisplayName("should authenticate with employee email identifier")
        void shouldAuthenticateWithEmployeeEmailIdentifier() {
            StaffAccount staffAccount = activeStaffAccount();

            when(staffAccountRepository.findByUsernameOrEmployeeEmail(EMAIL)).thenReturn(Optional.of(staffAccount));

            AuthCurrentUserResponseDTO response = authService.login(EMAIL, PASSWORD);

            assertThat(response.id()).isEqualTo(STAFF_ACCOUNT_ID);
            assertThat(response.username()).isEqualTo(USERNAME);
            assertThat(response.email()).isEqualTo(EMAIL);
            verify(staffAccountRepository).save(staffAccount);
        }

        @Test
        @DisplayName("should reject invalid password")
        void shouldRejectInvalidPassword() {
            when(staffAccountRepository.findByUsernameOrEmployeeEmail(USERNAME)).thenReturn(Optional.of(activeStaffAccount()));

            assertThatThrownBy(() -> authService.login(USERNAME, "senha-invalida"))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Credenciais inválidas");
        }

        @Test
        @DisplayName("should reject inactive internal user")
        void shouldRejectInactiveStaffAccount() {
            when(staffAccountRepository.findByUsernameOrEmployeeEmail(USERNAME)).thenReturn(Optional.of(inactiveStaffAccount()));

            assertThatThrownBy(() -> authService.login(USERNAME, PASSWORD))
                .isInstanceOf(DisabledException.class)
                .hasMessage("Usuário interno inativo");

            verify(staffAccountRepository).findByUsernameOrEmployeeEmail(USERNAME);
        }
    }

    @Nested
    @DisplayName("Current user")
    class CurrentUser {

        @Test
        @DisplayName("should map authenticated principal to current-user dto")
        void shouldMapAuthenticatedPrincipalToCurrentUserDto() {
            StaffAccount staffAccount = activeStaffAccount();

            AuthCurrentUserResponseDTO response = authService.getCurrentUser(staffAccount);

            assertThat(response)
                .extracting(
                    AuthCurrentUserResponseDTO::id,
                    AuthCurrentUserResponseDTO::username,
                    AuthCurrentUserResponseDTO::displayName,
                    AuthCurrentUserResponseDTO::email,
                    AuthCurrentUserResponseDTO::employeeId,
                    AuthCurrentUserResponseDTO::duty
                )
                .containsExactly(STAFF_ACCOUNT_ID, USERNAME, "Beatriz Santos", EMAIL, EMPLOYEE_ID, Duty.ADM);
        }
    }

    private StaffAccount activeStaffAccount() {
        return staffAccount(true);
    }

    private StaffAccount inactiveStaffAccount() {
        return staffAccount(false);
    }

    private StaffAccount staffAccount(boolean active) {
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

        StaffAccount staffAccount = new StaffAccount(employee, USERNAME, passwordEncoder.encode(PASSWORD), active);
        staffAccount.setId(STAFF_ACCOUNT_ID);
        return staffAccount;
    }
}
