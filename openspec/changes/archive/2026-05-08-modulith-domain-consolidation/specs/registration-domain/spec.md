## ADDED Requirements

### Requirement: Macro-módulo de Registro
O sistema SHALL consolidar as funcionalidades de estudantes, responsáveis, colaboradores e endereços sob um único domínio denominado `registration`.

#### Scenario: Estrutura de pacotes unificada
- **WHEN** o desenvolvedor inspeciona a árvore de domínios
- **THEN** as funcionalidades de cadastro devem estar aninhadas sob `com.aprimorar.api.domain.registration`

### Requirement: Relacionamento Direto entre Estudante e Responsável
Dentro do módulo `registration`, a entidade `Student` SHALL referenciar a entidade `Parent` diretamente via relacionamento JPA, em vez de utilizar apenas o ID.

#### Scenario: Navegação de objeto
- **WHEN** o sistema recupera um objeto `Student` do banco de dados no módulo de registro
- **THEN** deve ser possível acessar os dados do `Parent` associado sem realizar uma nova consulta explícita via service.
