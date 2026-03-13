# Relatorio de Oportunidades de Refatoracao

Este documento revisa o frontend React/Vite atual com base nas boas praticas de React da Vercel e destaca as oportunidades de refatoracao de maior valor encontradas no projeto.

## Escopo Revisado

- `src/App.tsx`
- `src/services/api.ts`
- `src/lib/schemas/*.ts`
- `src/features/dashboard/*.tsx`
- `src/features/students/*.tsx`
- `src/features/employees/*.tsx`
- `src/features/events/*.tsx`

## Prioridade 1: Divisao de Codigo por Rota

Regra relacionada: `bundle-dynamic-imports`

### Por que isso importa

`src/App.tsx` importa todas as paginas de forma antecipada. Conforme a aplicacao cresce, cada nova rota aumenta o bundle inicial e deixa o primeiro carregamento mais lento.

### Padrao atual

```tsx
import { DashboardPage } from "@/features/dashboard/DashboardPage"
import { StudentsPage } from "@/features/students/StudentsPage"
import { StudentDetailPage } from "@/features/students/StudentDetailPage"
import { StudentCreatePage } from "@/features/students/StudentCreatePage"
import { EmployeesPage } from "@/features/employees/EmployeesPage"
import { EmployeeDetailPage } from "@/features/employees/EmployeeDetailPage"
import { EmployeeCreatePage } from "@/features/employees/EmployeeCreatePage"
import { EventsPage } from "@/features/events/EventsPage"
import { EventDetailPage } from "@/features/events/EventDetailPage"
import { EventCreatePage } from "@/features/events/EventCreatePage"
```

### Direcao de refatoracao

Use `React.lazy` com `Suspense`, ou rotas lazy do React Router, para que cada tela seja carregada sob demanda.

### Exemplo

```tsx
import { Suspense, lazy } from "react"
import { BrowserRouter, Route, Routes } from "react-router-dom"
import { MainLayout } from "@/components/layout/MainLayout"
import { LoadingState } from "@/components/ui/loading-state"

const DashboardPage = lazy(() => import("@/features/dashboard/DashboardPage").then((m) => ({ default: m.DashboardPage })))
const StudentsPage = lazy(() => import("@/features/students/StudentsPage").then((m) => ({ default: m.StudentsPage })))
const StudentDetailPage = lazy(() => import("@/features/students/StudentDetailPage").then((m) => ({ default: m.StudentDetailPage })))

export default function App() {
  return (
    <BrowserRouter>
      <Suspense fallback={<LoadingState message="Carregando tela..." />}>
        <Routes>
          <Route element={<MainLayout />}>
            <Route path="/" element={<DashboardPage />} />
            <Route path="/students" element={<StudentsPage />} />
            <Route path="/students/:id" element={<StudentDetailPage />} />
          </Route>
        </Routes>
      </Suspense>
    </BrowserRouter>
  )
}
```

### Arquivos do projeto afetados

- `src/App.tsx`

## Prioridade 2: Substituir o Padrao Repetido de `useEffect + useState` para Busca de Dados

Regras relacionadas: `async-parallel`, `client-swr-dedup`, `rerender-functional-setstate`

### Por que isso importa

O projeto repete o mesmo padrao para estado de carregamento, estado de erro, tentativas novamente e chamadas de API. Isso aumenta o custo de manutencao e cria comportamentos inconsistentes.

Exemplos:

- `src/features/students/StudentsPage.tsx`
- `src/features/employees/EmployeesPage.tsx`
- `src/features/events/EventsPage.tsx`
- `src/features/students/StudentDetailPage.tsx`
- `src/features/employees/EmployeeDetailPage.tsx`
- `src/features/dashboard/DashboardPage.tsx`

### Padrao atual

```tsx
const [loading, setLoading] = useState(true)
const [error, setError] = useState<string | null>(null)
const [items, setItems] = useState<Item[]>([])

useEffect(() => {
  const loadItems = async () => {
    try {
      setError(null)
      setLoading(true)
      const res = await apiCall()
      setItems(res.data.content)
    } catch (error) {
      setError(getFriendlyErrorMessage(error))
    } finally {
      setLoading(false)
    }
  }

  loadItems()
}, [])
```

### Direcao de refatoracao

Duas boas opcoes:

1. Introduzir TanStack Query para cache, deduplicacao, invalidacao e tentativas novamente
2. Mover o carregamento de dados das rotas para loaders do React Router, se a ideia for deixar a busca de dados sob responsabilidade da rota

### Exemplo com hook de feature

```tsx
import { useQuery } from "@tanstack/react-query"
import { studentsApi } from "@/services/api"

export function useStudents() {
  return useQuery({
    queryKey: ["students"],
    queryFn: async () => {
      const response = await studentsApi.list(0, 20, "name")
      return response.data.content
    },
  })
}
```

```tsx
const { data: students = [], isLoading, error, refetch } = useStudents()

if (isLoading) return <LoadingState message="Carregando alunos..." />

if (error) {
  return (
    <ErrorState
      title="Nao foi possivel carregar"
      description={getFriendlyErrorMessage(error)}
      actionLabel="Tentar novamente"
      onAction={() => void refetch()}
    />
  )
}
```

### Arquivos do projeto afetados

- `src/features/students/StudentsPage.tsx`
- `src/features/employees/EmployeesPage.tsx`
- `src/features/events/EventsPage.tsx`
- `src/features/dashboard/DashboardPage.tsx`
- `src/features/students/StudentDetailPage.tsx`
- `src/features/employees/EmployeeDetailPage.tsx`
- `src/features/events/EventDetailPage.tsx`
- `src/features/students/StudentCreatePage.tsx`
- `src/features/events/EventCreatePage.tsx`

## Prioridade 3: Validar Respostas da API com Zod em Tempo de Execucao

Regra relacionada: `server-serialization`

### Por que isso importa

O codigo ja define schemas Zod em `src/lib/schemas`, mas as respostas da API sao tratadas como valores confiaveis do TypeScript. Se os DTOs do backend mudarem, a interface pode falhar depois e de forma mais dificil de diagnosticar.

### Padrao atual

```ts
const eventsRes = await eventsApi.list()
const eventsPage: PageResponse<EventResponse> = eventsRes.data
setEventList(eventsPage.content)
```

### Direcao de refatoracao

Fazer o parse das respostas dentro da camada de servico para que cargas invalidas falhem perto da fronteira de rede.

### Exemplo

```ts
import { z } from "zod"
import { eventResponseSchema } from "@/lib/schemas/event"

const pageSchema = <T extends z.ZodTypeAny>(itemSchema: T) =>
  z.object({
    content: z.array(itemSchema),
    number: z.number(),
    size: z.number(),
    totalElements: z.number(),
    totalPages: z.number(),
    first: z.boolean(),
    last: z.boolean(),
  })

export const eventsApi = {
  list: async () => {
    const response = await api.get("/v1/events?page=0&size=20&sort=startDateTime")
    return pageSchema(eventResponseSchema).parse(response.data)
  },
}
```

### Arquivos do projeto afetados

- `src/services/api.ts`
- `src/lib/schemas/student.ts`
- `src/lib/schemas/employee.ts`
- `src/lib/schemas/event.ts`
- `src/lib/schemas/parent.ts`

## Prioridade 4: Corrigir a Confiabilidade dos KPIs do Dashboard

Regras relacionadas: `async-parallel`, `js-early-exit`

### Por que isso importa

`src/features/dashboard/DashboardPage.tsx` usa endpoints paginados de listagem e calcula a receita com base em `eventsPage.content`, que inclui apenas a primeira pagina. Os contadores usam `totalElements`, mas a receita fica incorreta quando existem mais de 20 eventos.

### Padrao atual

```tsx
const [studentsRes, employeesRes, eventsRes] = await Promise.all([
  studentsApi.list(0, 20, "name"),
  employeesApi.list(),
  eventsApi.list(),
])

const total = eventsPage.content.reduce((sum, event) => sum + Number(event.payment), 0)
```

### Direcao de refatoracao

Prefira um endpoint agregado no backend, por exemplo `/v1/dashboard/summary`, em vez de remontar os KPIs no cliente.

### Exemplo de formato de DTO

```ts
type DashboardSummary = {
  studentsCount: number
  employeesCount: number
  eventsCount: number
  totalPayment: number
}
```

```tsx
const summary = await dashboardApi.getSummary()
setStudentsCount(summary.studentsCount)
setEmployeesCount(summary.employeesCount)
setEventsCount(summary.eventsCount)
setRevenue(summary.totalPayment)
```

### Arquivos do projeto afetados

- `src/features/dashboard/DashboardPage.tsx`
- contraparte de backend recomendada em `server/api-aprimorar/`

## Prioridade 5: Remover Truncamento Silencioso nas Telas com `size = 100`

Regras relacionadas: `client-swr-dedup`, `js-early-exit`

### Por que isso importa

Varias telas assumem que `100` registros sao suficientes para dados relacionados ou opcoes de selecao. Isso funciona com bases pequenas, mas vai cortar resultados silenciosamente conforme o projeto crescer.

Exemplos:

- `src/features/students/StudentDetailPage.tsx` usa `eventsApi.listByStudent(id, 0, 100, ...)`
- `src/features/employees/EmployeeDetailPage.tsx` usa `eventsApi.listByEmployee(id, 0, 100, ...)`
- `src/features/events/EventCreatePage.tsx` usa `studentsApi.list(0, 100, ...)` e `employeesApi.list(0, 100, ...)`
- `src/features/students/StudentCreatePage.tsx` usa `parentsApi.list(0, 100, ...)`

### Direcao de refatoracao

Use uma destas abordagens:

1. Adicionar tabelas realmente paginadas e seletores pesquisaveis
2. Adicionar endpoints dedicados e leves para opcoes de selecao
3. Nas secoes de eventos vinculados, adicionar controles de paginacao em vez de usar um limite arbitrario

### Exemplo de contrato para endpoint de opcoes

```ts
type SelectOption = {
  id: string
  label: string
}
```

```tsx
const students = await studentOptionsApi.list({ query: search, page: 0, size: 20 })
```

### Arquivos do projeto afetados

- `src/features/events/EventCreatePage.tsx`
- `src/features/students/StudentCreatePage.tsx`
- `src/features/students/StudentDetailPage.tsx`
- `src/features/employees/EmployeeDetailPage.tsx`

## Prioridade 6: Extrair Helpers Compartilhados de Apresentacao

Regras relacionadas: `rerender-memo`, `rendering-hoist-jsx`

### Por que isso importa

Existe duplicacao clara em pequenos helpers de apresentacao e mapas de dominio.

Exemplos encontrados:

- `SummaryField` duplicado em:
  - `src/features/students/StudentDetailPage.tsx`
  - `src/features/employees/EmployeeDetailPage.tsx`
  - `src/features/events/EventDetailPage.tsx`
- `dutyLabels` duplicado em:
  - `src/features/employees/EmployeeCreatePage.tsx`
  - `src/features/employees/EmployeesPage.tsx`
  - `src/features/employees/EmployeeDetailPage.tsx`

### Direcao de refatoracao

Mover UI reutilizavel e helpers de dominio para modulos compartilhados.

### Exemplo

```tsx
type SummaryFieldProps = {
  label: string
  value: string
  className?: string
}

export function SummaryField({ label, value, className }: SummaryFieldProps) {
  return (
    <div className={className}>
      <p>{label}</p>
      <p>{value}</p>
    </div>
  )
}
```

```ts
export const dutyLabels = {
  TEACHER: "Professor(a)",
  ADM: "Administrativo",
  THERAPIST: "Terapeuta",
  MENTOR: "Mentor(a)",
} as const
```

### Destino sugerido

- `src/components/ui/summary-field.tsx`
- `src/features/employees/dutyLabels.ts`

## Prioridade 7: Substituir Tentativa com Recarregamento Total da Pagina

Regra relacionada: `rerender-move-effect-to-event`

### Por que isso importa

`src/features/dashboard/DashboardPage.tsx` faz nova tentativa usando `window.location.reload()`. Isso descarta o estado da aplicacao, causa um recarregamento completo do documento e fica inconsistente com o comportamento de tentativa usado no restante do projeto.

### Padrao atual

```tsx
<ErrorState
  title="Ops, nao foi possivel carregar"
  description={error}
  actionLabel="Tentar novamente"
  onAction={() => window.location.reload()}
/>
```

### Padrao melhor

```tsx
const loadDashboard = useCallback(async () => {
  // logica atual de busca
}, [])

useEffect(() => {
  void loadDashboard()
}, [loadDashboard])

<ErrorState
  title="Ops, nao foi possivel carregar"
  description={error}
  actionLabel="Tentar novamente"
  onAction={() => void loadDashboard()}
/>
```

### Arquivos do projeto afetados

- `src/features/dashboard/DashboardPage.tsx`

## Ordem Sugerida de Refatoracao

### Alto impacto, baixo esforco

1. Extrair `dutyLabels` e `SummaryField` compartilhados
2. Substituir `window.location.reload()` por nova busca local
3. Remover suposicoes de `size = 100` das telas criticas

### Alto impacto, esforco medio

1. Introduzir lazy loading por rota em `src/App.tsx`
2. Comecar a validar respostas de API em `src/services/api.ts`
3. Criar uma camada compartilhada de busca de dados para as paginas

### Alto impacto, maior esforco

1. Adicionar endpoints agregados dedicados para o dashboard
2. Substituir seletores ad hoc por endpoints paginados e pesquisaveis para opcoes

## Primeiro Marco Pratico

Se o projeto quiser o melhor retorno rapidamente, um bom primeiro marco seria:

1. Carregar todas as paginas por lazy loading
2. Adicionar uma biblioteca de query ou loaders de rota para comportamento compartilhado de busca
3. Fazer parse das respostas da API com Zod em `src/services/api.ts`
4. Introduzir um endpoint dedicado de resumo do dashboard

Esse conjunto melhora o tamanho do bundle, reduz codigo duplicado, torna a busca de dados mais confiavel e remove um problema de corretude no dashboard.
