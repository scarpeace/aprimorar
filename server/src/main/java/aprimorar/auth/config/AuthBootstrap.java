package aprimorar.auth.config;

import aprimorar.auth.application.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AuthBootstrap {

    @Bean
    ApplicationRunner adminBootstrap(
        AuthService authService,
        @Value("${aprimorar.admin-username}") String email,
        @Value("${aprimorar.admin-password}") String password
    ) {
        return args -> authService.ensureAdmin(email, password);
    }
}
