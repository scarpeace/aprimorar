## Why
A estrutura atual de micro-módulos (student, parent, employee, address) gera ciclos de dependência artificiais e dificulta o uso de relacionamentos JPA naturais. Esta consolidação visa agrupar os domínios em três macro-contextos (registration, events, finance) para simplificar a arquitetura e cumprir as regras de verificação estrita do Spring Modulith.

## What Changes
- **BREAKING**: Criação do módulo macro `registration` que absorve os pacotes `student`, `parent`, `employee` e `address`.
- **BREAKING**: Reorganização de pacotes para o novo padrão: `com.aprimorar.api.domain.registration.[student|parent|employee|address]`.
- Refatoração de `Student` para utilizar relacionamento JPA real com `Parent` (opcional, mas recomendado pela nova estrutura).
- Consolidação dos arquivos `package-info.java` e configurações do Modulith para os três novos módulos.
- Atualização de todos os imports e referências no projeto.

## Capabilities

### New Capabilities
- `registration-domain`: Define a nova fronteira do módulo de cadastros unificado, abrangendo Alunos, Responsáveis, Colaboradores e Endereços.

### Modified Capabilities
- `server-architecture`: Atualização da especificação de módulos do servidor para refletir a nova estrutura de três macro-módulos.

## Impact
- **Backend**: Mudança massiva de pacotes e imports.
- **Modulith**: Simplificação da árvore de dependências e eliminação dos ciclos `parent <-> student` e `address`.
- **Database**: Sem impacto direto no schema, apenas na forma como o JPA mapeia as relações.
