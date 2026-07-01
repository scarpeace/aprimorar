package aprimorar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.modulith.Modulithic;
import org.springframework.beans.factory.annotation.Value;

import aprimorar.auth.service.UserService;

@Modulithic(
		additionalPackages = "aprimorar",
		sharedModules = {"config", "shared"}
)
@SpringBootApplication
public class AprimorarAplication {

	public static void main(String[] args) {
		SpringApplication.run(AprimorarAplication.class, args);
	}

	@Bean
	public CommandLineRunner seedAdminUser(
		UserService userService,
		@Value("${aprimorar.admin-username:admin@aprimorar.com}") String adminUsername,
		@Value("${aprimorar.admin-password:}") String adminPassword
	) {
		return args -> userService.ensureAdminUser(adminUsername, adminPassword);
	}
}
