@ApplicationModule(
    id = "agendamento",
    displayName = "Agendamento",
    allowedDependencies = {
        "common",
        "financeiro::repasses-colaboradores-api",
        "financeiro::recebimentos-alunos-api"
    }
)
package aprimorar.agendamento;

import org.springframework.modulith.ApplicationModule;
