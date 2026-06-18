package aprimorar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.modulith.Modulithic;

import aprimorar.auth.AuthBootstrap;

@Modulithic(
		additionalPackages = "aprimorar",
		sharedModules = {"config", "shared"}
)
@SpringBootApplication
public class ApiAprimorarApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiAprimorarApplication.class, args);
	}

	 @Bean
	 public CommandLineRunner seedAdminUser(AuthBootstrap authBootstrap) {
	 	return args -> authBootstrap.ensureAdminUser();
	 }
}
