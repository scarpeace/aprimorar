package aprimorar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import aprimorar.auth.service.AuthService;

@SpringBootApplication
public class AprimorarAplication {

	public static void main(String[] args) {
		SpringApplication.run(AprimorarAplication.class, args);
	}

	@Bean
	public CommandLineRunner seedAdminUser(AuthService authService) {
		return args -> authService.ensureAdminUser();
	}
}
