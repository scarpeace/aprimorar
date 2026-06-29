package aprimorar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.modulith.Modulithic;

import aprimorar.auth.AuthBootstrap;

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
	public CommandLineRunner seedAdminUser(ObjectProvider<AuthBootstrap> authBootstrap) {
		return args -> authBootstrap.ifAvailable(AuthBootstrap::ensureAdminUser);
	}
}
