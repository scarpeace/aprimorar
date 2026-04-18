package com.aprimorar.api.domain.auth;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aprimorar.api.config.SecurityConfig;
import com.aprimorar.api.config.WebCorsConfig;
import com.aprimorar.api.domain.auth.dto.AuthCurrentUserResponseDTO;
import com.aprimorar.api.domain.auth.dto.AuthLoginRequestDTO;
import com.aprimorar.api.domain.student.StudentController;
import com.aprimorar.api.domain.student.StudentService;
import com.aprimorar.api.enums.Duty;
import com.aprimorar.api.exception.GlobalExceptionHandler;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = { AuthController.class, StudentController.class })
@Import({ SecurityConfig.class, WebCorsConfig.class, GlobalExceptionHandler.class })
class AuthControllerSecurityTest {

    private static final UUID INTERNAL_USER_ID = UUID.fromString("8ccdb801-d0af-4561-8d45-56d196350001");
    private static final UUID EMPLOYEE_ID = UUID.fromString("b71fa3e6-31f0-4ef5-a650-1bccae83302e");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private InternalUserDetailsService internalUserDetailsService;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private Clock applicationClock;

    @Nested
    @DisplayName("Authentication endpoints")
    class AuthenticationEndpoints {

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
                .andExpect(cookie().exists("JSESSIONID"))
                .andExpect(jsonPath("$.id").value(INTERNAL_USER_ID.toString()))
                .andExpect(jsonPath("$.username").value("beatriz.santos"))
                .andExpect(jsonPath("$.displayName").value("Beatriz Santos"))
                .andExpect(jsonPath("$.email").value("beatriz.santos@731aprimorar.dev"))
                .andExpect(jsonPath("$.employeeId").value(EMPLOYEE_ID.toString()))
                .andExpect(jsonPath("$.duty").value(Duty.ADM.name()));
        }

        @Test
        @DisplayName("should return current user for valid session")
        void shouldReturnCurrentUserForValidSession() throws Exception {
            mockMvc.perform(get("/v1/auth/me"))
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
            var loginResult = mockMvc.perform(post("/v1/auth/login")
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

            mockMvc.perform(post("/v1/auth/logout")
                    .with(csrf())
                    .session((org.springframework.mock.web.MockHttpSession) loginResult.getRequest().getSession(false)))
                .andExpect(status().isNoContent());

            mockMvc.perform(get("/v1/auth/me")
                    .session((org.springframework.mock.web.MockHttpSession) loginResult.getRequest().getSession(false)))
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
            INTERNAL_USER_ID,
            "beatriz.santos",
            "Beatriz Santos",
            "beatriz.santos@731aprimorar.dev",
            EMPLOYEE_ID,
            Duty.ADM
        );
    }
}
