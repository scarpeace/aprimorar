package aprimorar;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ModuleVerificationTest {

    private final ApplicationModules modules = ApplicationModules.of(AprimorarAplication.class);

    @Test
    void verifiesModularStructure() {
        modules.verify();
    }

    @Test
    void writesModulithDocumentation() {
        modules.verify();

        new Documenter(
            modules,
            Documenter.Options.defaults().withOutputFolder("src/main/resources/docs")
        )
            .writeDocumentation();
    }
}
