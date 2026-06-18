package aprimorar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.modulith.Modulithic;

@Modulithic(
		additionalPackages = "aprimorar",
		sharedModules = {"config", "shared"}
)
@SpringBootApplication
public class ApiAprimorarApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiAprimorarApplication.class, args);
	}

//	 @Bean
//	 public CommandLineRunner seedAdminUser(aprimorar.auth.AuthBootstrap authBootstrap) {
//	 	return args -> authBootstrap.ensureAdminUser();
//	 }
}
