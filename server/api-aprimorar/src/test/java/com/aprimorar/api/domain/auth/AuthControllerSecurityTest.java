package com.aprimorar.api.domain.auth;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aprimorar.api.config.SecurityConfig;
import com.aprimorar.api.config.WebCorsConfig;
import com.aprimorar.api.domain.auth.repository.StaffAccountRepository;
import com.aprimorar.api.domain.auth.dto.AuthCurrentUserResponseDTO;
import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.domain.student.StudentController;
import com.aprimorar.api.domain.student.StudentService;
import com.aprimorar.api.enums.Duty;
import com.aprimorar.api.exception.GlobalExceptionHandler;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = { AuthController.class, StudentController.class })
@Import({ SecurityConfig.class, WebCorsConfig.class, GlobalExceptionHandler.class, AuthService.class, StaffAccountDetailsService.class })
class AuthControllerSecurityTest {

    private static final UUID STAFF_ACCOUNT_ID = UUID.fromString("8ccdb801-d0af-4561-8d45-56d196350001");
    private static final UUID EMPLOYEE_ID = UUID.fromString("b71fa3e6-31f0-4ef5-a650-1bccae83302e");
    private static final Instant FIXED_INSTANT = Instant.parse("2026-04-18T12:00:00Z");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StaffAccountRepository staffAccountRepository;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private Clock applicationClock;

    @BeforeEach
    void setUp() {
        when(applicationClock.instant()).thenReturn(FIXED_INSTANT);
        when(applicationClock.getZone()).thenReturn(ZoneId.of("UTC"));
        when(staffAccountRepository.findByUsernameOrEmployeeEmail("beatriz.santos")).thenReturn(Optional.of(activeStaffAccount()));
        when(staffAccountRepository.findByUsernameOrEmployeeEmail("beatriz.santos@731aprimorar.dev"))
            .thenReturn(Optional.of(activeStaffAccount()));
    }

    @Nested
    @DisplayName("Authentication endpoints")
    class AuthenticationEndpoints {

        @Test
        @DisplayName("should issue CSRF cookie for SPA bootstrap and accept login with returned token")
        @WithAnonymousUser
        void shouldIssueCsrfCookieForSpaBootstrapAndAcceptLoginWithReturnedToken() throws Exception {
            MvcResult bootstrapResult = mockMvc.perform(get("/v1/auth/me"))
                .andExpect(status().isUnauthorized())
                .andReturn();

            Cookie csrfCookie = bootstrapResult.getResponse().getCookie("XSRF-TOKEN");

            org.assertj.core.api.Assertions.assertThat(csrfCookie).isNotNull();
            org.assertj.core.api.Assertions.assertThat(csrfCookie.getValue()).isNotBlank();

            mockMvc.perform(post("/v1/auth/login")
                    .cookie(csrfCookie)
                    .header("X-XSRF-TOKEN", csrfCookie.getValue())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                          \"identifier\": \"beatriz.santos\",
                          \"password\": \"admin123\"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("beatriz.santos"));
        }

        @Test
        @DisplayName("should login and establish server session")
        void shouldLoginAndEstablishServerSession() throws Exception {
            mockMvc.perform(post("/v1/auth/login")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      \"identifier\": \"beatriz.santos\",
                      \"password\": \"admin123\"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(STAFF_ACCOUNT_ID.toString()))
                .andExpect(jsonPath("$.username").value("beatriz.santos"))
                .andExpect(jsonPath("$.displayName").value("Beatriz Santos"))
                .andExpect(jsonPath("$.email").value("beatriz.santos@731aprimorar.dev"))
                .andExpect(jsonPath("$.employeeId").value(EMPLOYEE_ID.toString()))
                .andExpect(jsonPath("$.duty").value(Duty.ADM.name()))
                .andExpect(result -> org.assertj.core.api.Assertions.assertThat(result.getRequest().getSession(false)).isNotNull());
        }

        @Test
        @DisplayName("should return current user for valid session")
        void shouldReturnCurrentUserForValidSession() throws Exception {
            MvcResult loginResult = login();

            mockMvc.perform(get("/v1/auth/me")
                    .session((org.springframework.mock.web.MockHttpSession) loginResult.getRequest().getSession(false)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("beatriz.santos"));
        }

        @Test
        @DisplayName("should return unauthorized for missing session on current-user endpoint")
        @WithAnonymousUser
        void shouldReturnUnauthorizedForMissingSessionOnCurrentUserEndpoint() throws Exception {
            mockMvc.perform(get("/v1/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Usuário não autenticado"));
        }

        @Test
        @DisplayName("should invalidate session on logout")
        void shouldInvalidateSessionOnLogout() throws Exception {
            MvcResult loginResult = login();
            MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession();

            mockMvc.perform(post("/v1/auth/logout")
                    .with(csrf())
                    .session(session))
                .andExpect(status().isNoContent());

            mockMvc.perform(get("/v1/auth/me")
                    .session(session))
                .andExpect(status().isUnauthorized());
        }
    }

    @Test
    @DisplayName("should reject anonymous access to protected students endpoint")
    @WithAnonymousUser
    void shouldRejectAnonymousAccessToProtectedStudentsEndpoint() throws Exception {
        mockMvc.perform(get("/v1/students"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("Usuário não autenticado"));
    }

    private static AuthCurrentUserResponseDTO currentUser() {
        return new AuthCurrentUserResponseDTO(
            STAFF_ACCOUNT_ID,
            "beatriz.santos",
            "Beatriz Santos",
            "beatriz.santos@731aprimorar.dev",
            EMPLOYEE_ID,
            Duty.ADM
        );
    }

    private MvcResult login() throws Exception {
        return mockMvc.perform(post("/v1/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      \"identifier\": \"beatriz.santos\",
                      \"password\": \"admin123\"
                    }
                    """))
            .andExpect(status().isOk())
            .andReturn();
    }

    private static StaffAccount activeStaffAccount() {
        Employee employee = new Employee(
            "Beatriz Santos",
            LocalDate.of(2008, 12, 18),
            "33675240185",
            "61955228868",
            "89720151137",
            "beatriz.santos@731aprimorar.dev",
            Duty.ADM
        );
        employee.setId(EMPLOYEE_ID);

        StaffAccount staffAccount = new StaffAccount(
            employee,
            "beatriz.santos",
            "$2y$10$U06GVi2DgZtxl9XD0Th93.uBWF9dXUnvqgedCljpmsQh3M93zEeAq",
            true
        );
        staffAccount.setId(STAFF_ACCOUNT_ID);
        return staffAccount;
    }
}
