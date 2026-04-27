package com.aprimorar.api.config;

import com.aprimorar.api.exception.ErrorCode;
import com.aprimorar.api.exception.ProblemResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.function.Supplier;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final com.aprimorar.api.domain.auth.AuthService authService;
    private final ObjectMapper objectMapper;

    public SecurityConfig(
        com.aprimorar.api.domain.auth.AuthService authService,
        ObjectMapper objectMapper
    ) {
        this.authService = authService;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider)
        throws Exception {
        http
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                .ignoringRequestMatchers(new AntPathRequestMatcher("/v1/auth/login", "POST"))
            )
            .cors(Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .authenticationProvider(authenticationProvider)
            .addFilterAfter(new CsrfCookieFilter(), CsrfFilter.class)
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/v1/auth/login",
                    "/v1/auth/logout",
                    "/v1/auth/me",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                )
                .permitAll()
                .requestMatchers("/v1/**").authenticated()
                .anyRequest().permitAll()
            )
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(authService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) ->
            writeProblemResponse(response, HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED, "Usuário não autenticado", request.getRequestURI());
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) ->
            writeProblemResponse(response, HttpStatus.FORBIDDEN, ErrorCode.BUSINESS_ERROR, "Acesso negado", request.getRequestURI());
    }

    private void writeProblemResponse(
        jakarta.servlet.http.HttpServletResponse response,
        HttpStatus status,
        ErrorCode errorCode,
        String message,
        String uri
    ) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), new ProblemResponseDTO(errorCode, status, message, uri));
    }

    private static final class CsrfCookieFilter extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            Object attribute = request.getAttribute(CsrfToken.class.getName());

            if (attribute instanceof Supplier<?> csrfTokenSupplier) {
                Object token = csrfTokenSupplier.get();
                if (token instanceof CsrfToken csrfToken) {
                    csrfToken.getToken();
                }
            } else if (attribute instanceof CsrfToken csrfToken) {
                csrfToken.getToken();
            }

            filterChain.doFilter(request, response);
        }
    }

    private static final class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {

        private final CsrfTokenRequestHandler plainRequestHandler = new CsrfTokenRequestAttributeHandler();
        private final CsrfTokenRequestHandler xorRequestHandler = new XorCsrfTokenRequestAttributeHandler();

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
            xorRequestHandler.handle(request, response, csrfToken);
        }

        @Override
        public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
            if (StringUtils.hasText(request.getHeader(csrfToken.getHeaderName()))) {
                return plainRequestHandler.resolveCsrfTokenValue(request, csrfToken);
            }

            return xorRequestHandler.resolveCsrfTokenValue(request, csrfToken);
        }
    }
}
