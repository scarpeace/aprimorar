@ApplicationModule(
    id = "instituicao",
    displayName = "Instituição",
    allowedDependencies = {
        "common",
        "financeiro::cobrancas-api",
        "financeiro::repasses-api"
    }
)
package aprimorar.instituicao;

import org.springframework.modulith.ApplicationModule;
