package aprimorar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
}
