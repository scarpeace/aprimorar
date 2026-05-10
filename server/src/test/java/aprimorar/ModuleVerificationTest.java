package aprimorar;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModuleVerificationTest {

	@Test
	void verifyModuleStructure() {
		ApplicationModules.of(ApiAprimorarApplication.class).verify();
	}
}
