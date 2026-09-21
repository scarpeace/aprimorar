@ApplicationModule(
    id = "agendamento",
    displayName = "Agendamento",
    allowedDependencies = {
        "common",
        "financeiro::pagamentos-colaboradores-api",
        "financeiro::recebimentos-alunos-api"
    }
)
package aprimorar.agendamento;

import org.springframework.modulith.ApplicationModule;
