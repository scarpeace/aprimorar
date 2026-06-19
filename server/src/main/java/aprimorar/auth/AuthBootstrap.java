package aprimorar.auth;

import aprimorar.auth.domain.User;
import aprimorar.auth.infrastructure.persistence.UserRepository;
import aprimorar.shared.enums.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthBootstrap {

	private static final String ADMIN_EMAIL = "admin@aprimorar.com";
	private static final String ADMIN_PASSWORD = "Freerider";

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthBootstrap(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public void ensureAdminUser() {
		if (userRepository.existsByUsername(ADMIN_EMAIL)) {
			return;
		}

		User admin = new User(
			ADMIN_EMAIL,
			passwordEncoder.encode(ADMIN_PASSWORD),
			Role.ADMIN,
			true
		);

		userRepository.save(admin);
	}
}
