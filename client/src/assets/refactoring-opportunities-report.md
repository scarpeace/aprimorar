# Refactoring Opportunities Report

This document reviews the current React/Vite frontend against the Vercel React best practices and highlights the highest-value refactoring opportunities found in the project.

## Scope Reviewed

- `src/App.tsx`
- `src/services/api.ts`
- `src/lib/schemas/*.ts`
- `src/features/dashboard/*.tsx`
- `src/features/students/*.tsx`
- `src/features/employees/*.tsx`
- `src/features/events/*.tsx`

## Priority 1: Route-Based Code Splitting

Related rule: `bundle-dynamic-imports`

### Why this matters

`src/App.tsx` eagerly imports every page. As the app grows, each new route increases the initial bundle and makes first load slower.

### Current pattern

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

### Refactor direction

Use `React.lazy` with `Suspense`, or React Router lazy routes, so each screen loads on demand.

### Example

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

### Project files affected

- `src/App.tsx`

## Priority 2: Replace Repeated `useEffect + useState` Fetching

Related rules: `async-parallel`, `client-swr-dedup`, `rerender-functional-setstate`

### Why this matters

The project repeats the same pattern for loading state, error state, retries, and API calls. This increases maintenance cost and creates inconsistent behavior.

Examples:

- `src/features/students/StudentsPage.tsx`
- `src/features/employees/EmployeesPage.tsx`
- `src/features/events/EventsPage.tsx`
- `src/features/students/StudentDetailPage.tsx`
- `src/features/employees/EmployeeDetailPage.tsx`
- `src/features/dashboard/DashboardPage.tsx`

### Current pattern

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

### Refactor direction

Two good options:

1. Introduce TanStack Query for caching, deduping, invalidation, and retries
2. Move route data loading into React Router loaders if you want route-owned fetching

### Example with a feature hook

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

### Project files affected

- `src/features/students/StudentsPage.tsx`
- `src/features/employees/EmployeesPage.tsx`
- `src/features/events/EventsPage.tsx`
- `src/features/dashboard/DashboardPage.tsx`
- `src/features/students/StudentDetailPage.tsx`
- `src/features/employees/EmployeeDetailPage.tsx`
- `src/features/events/EventDetailPage.tsx`
- `src/features/students/StudentCreatePage.tsx`
- `src/features/events/EventCreatePage.tsx`

## Priority 3: Validate API Responses with Zod at Runtime

Related rule: `server-serialization`

### Why this matters

The code already defines Zod schemas in `src/lib/schemas`, but API responses are treated as trusted TypeScript values. If backend DTOs drift, the UI can fail later and in more confusing places.

### Current pattern

```ts
const eventsRes = await eventsApi.list()
const eventsPage: PageResponse<EventResponse> = eventsRes.data
setEventList(eventsPage.content)
```

### Refactor direction

Parse responses inside the service layer so invalid payloads fail near the network boundary.

### Example

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

### Project files affected

- `src/services/api.ts`
- `src/lib/schemas/student.ts`
- `src/lib/schemas/employee.ts`
- `src/lib/schemas/event.ts`
- `src/lib/schemas/parent.ts`

## Priority 4: Fix Dashboard KPI Correctness

Related rules: `async-parallel`, `js-early-exit`

### Why this matters

`src/features/dashboard/DashboardPage.tsx` uses paginated list endpoints and calculates revenue from `eventsPage.content`, which only includes the first page. The count values use `totalElements`, but the revenue value becomes wrong when there are more than 20 events.

### Current pattern

```tsx
const [studentsRes, employeesRes, eventsRes] = await Promise.all([
  studentsApi.list(0, 20, "name"),
  employeesApi.list(),
  eventsApi.list(),
])

const total = eventsPage.content.reduce((sum, event) => sum + Number(event.payment), 0)
```

### Refactor direction

Prefer a dedicated backend aggregate endpoint, for example `/v1/dashboard/summary`, instead of reconstructing KPIs on the client.

### Example DTO shape

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

### Project files affected

- `src/features/dashboard/DashboardPage.tsx`
- backend counterpart recommended in `server/api-aprimorar/`

## Priority 5: Remove Silent Truncation from `size = 100` Screens

Related rules: `client-swr-dedup`, `js-early-exit`

### Why this matters

Several screens assume `100` records is enough for related data or select options. That works in small datasets but will silently cut results as the project grows.

Examples:

- `src/features/students/StudentDetailPage.tsx` uses `eventsApi.listByStudent(id, 0, 100, ...)`
- `src/features/employees/EmployeeDetailPage.tsx` uses `eventsApi.listByEmployee(id, 0, 100, ...)`
- `src/features/events/EventCreatePage.tsx` uses `studentsApi.list(0, 100, ...)` and `employeesApi.list(0, 100, ...)`
- `src/features/students/StudentCreatePage.tsx` uses `parentsApi.list(0, 100, ...)`

### Refactor direction

Use one of these approaches:

1. Add true paginated tables and searchable selectors
2. Add dedicated lightweight endpoints for select options
3. For linked-event sections, add pagination controls instead of pulling an arbitrary cap

### Example option endpoint contract

```ts
type SelectOption = {
  id: string
  label: string
}
```

```tsx
const students = await studentOptionsApi.list({ query: search, page: 0, size: 20 })
```

### Project files affected

- `src/features/events/EventCreatePage.tsx`
- `src/features/students/StudentCreatePage.tsx`
- `src/features/students/StudentDetailPage.tsx`
- `src/features/employees/EmployeeDetailPage.tsx`

## Priority 6: Extract Shared Presentation Helpers

Related rules: `rerender-memo`, `rendering-hoist-jsx`

### Why this matters

There is clear duplication in small presentational helpers and lookup maps.

Examples found:

- `SummaryField` duplicated in:
  - `src/features/students/StudentDetailPage.tsx`
  - `src/features/employees/EmployeeDetailPage.tsx`
  - `src/features/events/EventDetailPage.tsx`
- `dutyLabels` duplicated in:
  - `src/features/employees/EmployeeCreatePage.tsx`
  - `src/features/employees/EmployeesPage.tsx`
  - `src/features/employees/EmployeeDetailPage.tsx`

### Refactor direction

Move reusable UI and domain helpers into shared modules.

### Example

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

### Suggested destination

- `src/components/ui/summary-field.tsx`
- `src/features/employees/dutyLabels.ts`

## Priority 7: Replace Full Page Reload Retry Behavior

Related rule: `rerender-move-effect-to-event`

### Why this matters

`src/features/dashboard/DashboardPage.tsx` retries by calling `window.location.reload()`. This throws away app state, causes a full document reload, and is inconsistent with the retry behavior used elsewhere.

### Current pattern

```tsx
<ErrorState
  title="Ops, nao foi possivel carregar"
  description={error}
  actionLabel="Tentar novamente"
  onAction={() => window.location.reload()}
/>
```

### Better pattern

```tsx
const loadDashboard = useCallback(async () => {
  // current fetch logic
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

### Project files affected

- `src/features/dashboard/DashboardPage.tsx`

## Suggested Refactor Order

### High impact, low effort

1. Extract shared `dutyLabels` and `SummaryField`
2. Replace `window.location.reload()` with local refetch
3. Remove `size = 100` assumptions from critical screens

### High impact, medium effort

1. Introduce route-based lazy loading in `src/App.tsx`
2. Start validating API responses in `src/services/api.ts`
3. Create a shared data-fetching layer for pages

### High impact, higher effort

1. Add dedicated dashboard aggregate endpoints
2. Replace ad hoc selectors with searchable paginated option endpoints

## Practical First Milestone

If the project wants the best return quickly, a good first milestone would be:

1. Lazy load all route pages
2. Add a query library or route loaders for shared fetching behavior
3. Parse API responses with Zod in `src/services/api.ts`
4. Introduce a dedicated dashboard summary endpoint

This set would improve bundle size, reduce duplicated code, make data fetching more reliable, and remove one correctness issue in the dashboard.
