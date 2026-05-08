## Context
Atualmente, o sistema possui 7 módulos de domínio fragmentados. Essa fragmentação excessiva gera acoplamento cíclico e dificulta o uso de recursos do JPA (como `@ManyToOne`) para entidades que são naturalmente ligadas (ex: Estudante e Responsável).

## Goals / Non-Goals

**Goals:**
- Consolidar `student`, `parent`, `employee` e `address` no macro-módulo `registration`.
- Eliminar ciclos de dependência entre os domínios de cadastro.
- Facilitar relacionamentos JPA internos no módulo `registration`.
- Manter o isolamento entre `registration`, `events` e `finance`.

**Non-Goals:**
- Mudar regras de negócio ou lógica de serviços.
- Alterar a estrutura do frontend.
- Migrar para Microserviços (o objetivo continua sendo um Modular Monolith).

## Decisions

- **Estrutura de Pacotes**: Adotar o padrão `com.aprimorar.api.domain.registration.[feature]`. Ex: `com.aprimorar.api.domain.registration.student.internal.Student`.
- **Relacionamento Student-Parent**: No novo módulo `registration`, a entidade `Student` voltará a ter uma referência direta `@ManyToOne` para `Parent`, eliminando o campo `parentId` manual e lookups síncronos via service apenas para esse vínculo.
- **Isolamento de API**: Cada sub-feature (student, employee, etc.) dentro de `registration` manterá seu pacote `api` para DTOs e Exceptions, mas o módulo `registration` terá um único `package-info.java` na sua raiz.
- **Shared Entities**: O pacote `address` será movido para dentro de `registration` como um sub-pacote compartilhado internamente ou uma entidade comum.

## Risks / Trade-offs

- **[Risk] Refatoração Massiva** → Mitigation: Realizar a movimentação de arquivos e ajuste de pacotes de forma automatizada (via IDE ou scripts de substituição) e validar com build/test-compile a cada etapa.
- **[Risk] Quebra de Injeção de Dependência** → Mitigation: Garantir que as anotações `@Service` e `@Repository` permaneçam em pacotes escaneados pelo Spring.
