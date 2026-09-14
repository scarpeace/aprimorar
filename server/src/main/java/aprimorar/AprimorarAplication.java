package aprimorar;

import aprimorar.auth.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AprimorarAplication {

	public static void main(String[] args) {
		SpringApplication.run(AprimorarAplication.class, args);
	}

	@Bean
	ApplicationRunner adminBootstrap(
		AuthService authService,
		@Value("${aprimorar.admin-username}") String email,
		@Value("${aprimorar.admin-password}") String password
	) {
		return args -> authService.ensureAdmin(email, password);
	}

}
