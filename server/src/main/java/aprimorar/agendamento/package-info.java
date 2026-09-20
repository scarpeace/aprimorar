@ApplicationModule(
    id = "agendamento",
    displayName = "Agendamento",
    allowedDependencies = {
        "common",
        "financeiro::financeiro-aluno-api",
        "financeiro::repasses-api"
    }
)
package aprimorar.agendamento;

import org.springframework.modulith.ApplicationModule;
