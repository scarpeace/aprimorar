# EITA SHARED - Plano de Limpeza de Fronteiras de Módulos

Este plano organiza a limpeza de responsabilidades entre módulos no backend, com foco em reduzir acoplamento no pacote `shared`, explicitar contratos `api` e manter `internal` realmente interno.

## Objetivos

- Reduzir uso genérico de `shared` para apenas o que for realmente transversal.
- Tornar explícito o que cada módulo expõe como API pública (`.../api`).
- Evitar que outros módulos dependam de detalhes internos (`.../internal`).
- Melhorar legibilidade arquitetural e aderência ao Spring Modulith.

## Critérios de Decisão

- Se um tipo é usado por vários módulos e representa contrato transversal estável, pode ficar em `shared`.
- Se um tipo pertence claramente a um domínio (ex.: cadastro), deve morar no módulo dono.
- Métodos em interfaces `...api.Service` devem existir apenas quando consumidos fora do módulo.
- Event listeners entre módulos devem consumir eventos/contratos públicos, nunca classes `internal`.

## Fase 1 - Mapeamento de Dependências (sem refactor)

- Levantar todos os usos de:
  - `shared.enums.Role`
  - `shared.enums.Duty`
  - `shared.Person`
  - `shared.MapperUtils`
  - `shared.exception.*`
- Produzir matriz `tipo -> consumidores por módulo` (`auth`, `registration`, `appointment`, `expense`, `dashboard`).
- Identificar dependências cruzadas indevidas em `internal`.

Status: [X] Concluída

Resultado do mapeamento:

- `shared.enums.Role`
  - Consumidores: `auth`, `registration` e `shared.Person`.
  - Decisão: manter em `shared` por ser transversal.
- `shared.enums.Duty`
  - Consumidores: `registration.employee` e `appointment.api.dto.EmployeeWithFinanceDTO`.
  - Decisão: mover para `registration.employee.api`.
- `shared.Person`
  - Consumidores: apenas `registration.student`, `registration.parent`, `registration.employee`.
  - Decisão: mover para `registration.shared`.
- `shared.MapperUtils`
  - Consumidores: `auth`, `registration.employee` e `Person`.
  - Decisão: manter por enquanto em `shared` (avaliar fatiamento futuro por domínio).
- `shared.exception.*`
  - `ProblemResponseDTO` e `GlobalExceptionHandler` são realmente transversais.
  - `DomainBusinessException` estava sem uso e foi removida.

## Fase 2 - Contratos Públicos dos Serviços

- Revisar cada `...api.Service` e seu `...internal/*ServiceImpl`.
- Classificar método por método:
  - `PUBLIC_API`: consumido por outro módulo.
  - `INTERNAL_ONLY`: usado apenas no módulo.
- Remover das interfaces os métodos `INTERNAL_ONLY`.
- Manter no `impl` os métodos internos e helpers.

## Fase 3 - Movimentação de Tipos de Domínio fora de `shared`

### 3.1 Baixo risco (primeiro)

- Mover `Duty` para `registration` (provável `registration.employee` ou `registration.shared`).
- Ajustar imports e validar compilação.

Status: [X] Concluída

- Implementado em `registration.employee.api.Duty`.
- Imports atualizados em `registration.employee` e `appointment.api.dto.EmployeeWithFinanceDTO`.

### 3.2 Médio risco

- Mover `Person` para `registration/shared` (ou subpacote equivalente do módulo de cadastro).
- Ajustar heranças (`Student`, `Parent`, `Employee`) e imports de outros módulos.
- Revalidar fronteiras de módulo após mudança.

Status: [X] Concluída

- Implementado em `registration.shared.Person`.
- Heranças e imports de `Student`, `Parent` e `Employee` atualizados.

### 3.3 `Role` (decisão guiada por uso real)

- Se uso principal for auth/autorização, avaliar mover para `auth`.
- Se continuar estruturalmente transversal, manter compartilhado, mas com fronteira explícita e documentação.

Status: [X] Decisão tomada

- `Role` permanece em `shared`.

## Fase 4 - Limpeza de Exceções

- Manter em `shared.exception` apenas o que for global/transversal:
  - `ProblemResponseDTO`
  - `GlobalExceptionHandler` (ou equivalente global)
- Exceções de negócio devem ficar nos módulos donos (ex.: `auth/api/exception`).
- Evitar exceções de domínio específico no `shared`.

Status: [~] Parcial

- `DomainBusinessException` removida de `shared.exception` por não uso.
- `ProblemResponseDTO` e `GlobalExceptionHandler` mantidos como transversais.

## Fase 5 - Verificação Arquitetural

- Compilação: `./mvnw clean compile`
- Verificação de módulos: `./mvnw test -Dtest=ModuleVerificationTest`
- Revisar se docs do Modulith foram impactados (`server/src/main/resources/docs/`).

Status: [X] Executada nesta etapa

- `./mvnw clean compile` executado com sucesso.
- `./mvnw test -Dtest=ModuleVerificationTest` executado com sucesso.

## Estratégia de Execução

- Executar em lotes pequenos e reversíveis.
- Validar a cada lote (compile + module verification quando necessário).
- Evitar big-bang de múltiplos módulos sem checkpoint.

## Primeiros Lotes Recomendados

1. Fase 1 completa (mapeamento) + relatório curto no próprio arquivo.
2. Fase 2 no módulo `auth` e `registration`.
3. Fase 3.1 (`Duty`) com validação.
4. Fase 3.2 (`Person`) com validação de Modulith.
5. Fase 4 refinando `shared.exception`.

## Observações

- Mudanças de pacote podem impactar geração de cliente/OpenAPI indiretamente via tipos e imports.
- Sempre priorizar clareza de fronteira de módulo sobre conveniência de curto prazo.
