# Plano de melhoria da estrutura do client

## Objetivo

Aproximar a organizacao do frontend da ideia de modulos do backend: cada feature continua sendo autonoma, mas passa a ter uma fronteira publica mais clara e uma estrutura mais consistente.

## Diagnostico atual

- A separacao por feature ja esta boa (`appointments`, `students`, `parents`, `employees`, etc.).
- Falta uma fronteira explicita entre o que e publico e o que e interno de cada feature.
- Existem inconsistencias de nomenclatura e organizacao entre features.
- Existem alguns typos em nomes de arquivos.

## Direcao proposta

### 1. Criar uma API publica por feature com `index.ts`

Cada feature deve expor apenas o que outras partes da aplicacao realmente precisam consumir.

Exemplo:

```ts
// features/appointments/index.ts
export { AppointmentsPage } from "./pages/AppointmentsPage";
export { AppointmentDetailPage } from "./pages/AppointmentDetailPage";
```

Regra pratica:

- Outras features devem importar de `features/<feature>/index.ts`.
- Componentes, hooks e helpers internos continuam dentro da propria feature.
- Se algo comeca a ser usado por varias features, ele deve migrar para `components/` ou `lib/`.

Isso traz para o client a mesma ideia do backend:

- `api/` no backend -> `index.ts` como superficie publica no frontend.
- `internal/` no backend -> pastas internas da feature (`components`, `hooks`, `forms`, `utils`).

### 2. Padronizar a estrutura minima por feature

Estrutura alvo:

```text
features/<feature>/
  components/
  forms/
  hooks/
  pages/
  utils/
  index.ts
```

Observacoes:

- Nem toda feature precisa de todas as pastas.
- O importante e seguir a mesma logica quando a pasta existir.
- `dashboard` deve seguir o mesmo padrao das outras features.

### 3. Corrigir inconsistencias atuais

Aplicar estas limpezas:

- Mover `client/src/features/dashboard/DashboardPage.tsx` para `client/src/features/dashboard/pages/DashboardPage.tsx`.
- Remover `client/src/features/finance/lib/` se continuar vazio.
- Padronizar o naming dos arquivos de hooks entre features.

### 4. Unificar convencoes de nomenclatura

Sugestao:

- `PascalCase` para componentes React e pages.
- `kebab-case` para hooks, utilitarios compartilhados e arquivos de infra.
- `camelCase` apenas onde o time realmente considerar mais natural, mas com regra clara.

Ponto principal: escolher um padrao unico para hooks e manter esse padrao em todas as features.

### 5. Corrigir typos de arquivos

- `client/src/features/employees/hooks/emlpoyee-mutations.ts` -> `employee-mutations.ts`
- `client/src/lib/shared/eventContentLables.ts` -> `eventContentLabels.ts`
- `client/src/lib/utils/dateFormater.ts` -> `dateFormatter.ts`

### 6. Adicionar barrels em compartilhados

Criar `index.ts` em pastas compartilhadas que ja funcionam como biblioteca interna:

- `client/src/components/ui/index.ts`
- `client/src/components/layout/index.ts`

Beneficio:

- imports mais simples
- superficie publica mais clara
- menos acoplamento a caminhos internos

## Ordem recomendada de implementacao

1. Corrigir typos e inconsistencias simples.
2. Mover `DashboardPage` para `pages/`.
3. Remover pasta vazia de `finance`.
4. Criar `index.ts` nas features mais consumidas externamente.
5. Criar barrels em `components/ui` e `components/layout`.
6. Revisar imports para passar a usar a superficie publica das features.

## Resultado esperado

- Client mais previsivel e consistente.
- Menos deep imports entre features.
- Fronteiras de modulo mais claras, em linha com a organizacao do backend.
- Melhor base para crescer sem espalhar dependencias internas entre features.
