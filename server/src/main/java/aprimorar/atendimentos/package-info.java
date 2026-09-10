@ApplicationModule(
    id = "atendimentos",
    displayName = "Atendimento",
    allowedDependencies = {"pessoas::aluno", "pessoas::colaborador", "common::*"}
)
package aprimorar.atendimentos;

import org.springframework.modulith.ApplicationModule;
