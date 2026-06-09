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

 // @Override
 //    public void run(ApplicationArguments args) {
 //        if (userRepository.existsByUsername("admin@aprimorar.com")) {
 //            return;
 //        }

 //        User user = new User(
 //            "admin@aprimorar.com",
 //            passwordEncoder.encode("Freerider"),
 //            Role.ADMIN
 //        );

 //        userRepository.save(user);
 //    }

}
