@ApplicationModule(
    id = "atendimentos",
    displayName = "Atendimento",
    allowedDependencies = {
        "pessoas::api",
        "common::*"
    }
)
package aprimorar.atendimentos;

import org.springframework.modulith.ApplicationModule;
