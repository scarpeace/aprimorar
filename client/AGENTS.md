# Frontend AGENTS.md

React + TypeScript + Vite patterns for the Aprimorar client application.

## Build Commands

```bash
npm run dev      # Start dev server (http://localhost:5173)
npm run lint     # Lint all TypeScript/TSX files
npm run build    # TypeScript type-check + production build
npm run preview  # Preview production build locally
```

## File Structure

```
client/src/
├── features/           # Route pages and feature-specific code
│   ├── students/
│   │   ├── StudentsPage.tsx
│   │   ├── StudentDetailPage.tsx
│   │   ├── StudentCreatePage.tsx
│   │   ├── components/    # Feature-specific components
│   │   └── hooks/         # Feature-specific hooks
│   └── events/
├── components/
│   └── ui/               # Reusable UI primitives
├── lib/
│   ├── schemas/          # Zod schemas per domain
│   └── query/            # React Query setup, queryKeys
└── services/             # API wrappers (Axios)
```

## Component Patterns

### File Naming
- Pages: `*Page.tsx` (e.g., `StudentsPage.tsx`)
- Components: `*.tsx` (e.g., `StudentsTable.tsx`)
- Hooks: `hooks/*.ts` or `use-*.ts`

### Export Pattern
Use **named exports** for all components:

```tsx
// Correct
export function StudentsPage() { ... }

// Avoid
export default function StudentsPage() { ... }
```

### Component Props
Define props as interfaces at the top of the file:

```tsx
interface PageHeaderProps {
  title: string
  description?: string
  action?: ReactNode
  Icon?: ElementType
}

export function PageHeader({ title, description, action, Icon, ... }: PageHeaderProps) {
  // ...
}
```

### Variant Pattern
For styled components with variants:

```tsx
type ButtonVariant = "primary" | "secondary" | "success" | "error" | "outline" | "ghost"

const VARIANT_CLASSES: Record<ButtonVariant, string> = {
  primary: "btn-primary",
  secondary: "btn-secondary",
  // ...
}

export function Button({ variant = "primary", className, ... }: ButtonProps) {
  return <button className={`${VARIANT_CLASSES[variant]} ${className ?? ""}`} {...} />
}
```

## Zod Schema Patterns

### Schema Organization
One file per domain in `lib/schemas/`:

```typescript
// student.ts
export const studentInputSchema = z.object({
  name: z.string().min(1, "Nome é obrigatório"),
  email: z.string().email("E-mail inválido"),
  birthdate: z.string().refine((d) => new Date(d) < new Date(), "Data deve ser no passado"),
  parentId: z.uuid(),
})

export const studentResponseSchema = z.object({
  id: z.uuid(),
  name: z.string(),
  // ...
})

export type StudentFormInput = z.infer<typeof studentInputSchema>
export type StudentResponse = z.infer<typeof studentResponseSchema>
```

### Generic Paginated Response
```typescript
export const pageResponseSchema = <T extends z.ZodTypeAny>(itemSchema: T) =>
  z.object({
    content: z.array(itemSchema),
    page: z.object({
      size: z.number(),
      totalElements: z.number(),
      totalPages: z.number(),
      number: z.number(),
    }),
  })
```

### Transform Patterns
```typescript
cpf: z.string().transform(formatCpf),
contact: z.string().transform(formatPhone),
```

## API Patterns

### Axios Configuration
```typescript
// services/api.ts
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080",
  headers: { "Content-Type": "application/json" },
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error("API Error:", error.response?.data || error.message)
    return Promise.reject(error)
  }
)
```

### API Wrapper Pattern
```typescript
export const studentsApi = {
  async list(page = 0, size = 50, search?: string): Promise<StudentResponse[]> {
    const { data } = await api.get("/v1/students", { params: { page, size, search } })
    return studentResponseSchema.parse(data)
  },

  async create(input: StudentFormInput): Promise<StudentResponse> {
    const { data } = await api.post("/v1/students", input)
    return studentResponseSchema.parse(data)
  },
}
```

### Error Handling
```typescript
// services/api-errors.ts
export function getFriendlyErrorMessage(error: unknown): string {
  if (!error) return ""

  if (error instanceof ZodError) {
    return "Resposta da API em formato inesperado"
  }

  if (axios.isAxiosError(error)) {
    const status = error.response?.status
    const apiMessage = error.response?.data?.message

    if (status === 400) return apiMessage ?? "Dados inválidos"
    if (status === 404) return apiMessage ?? "Não encontrado"
    if (status === 409) return apiMessage ?? "Conflito de dados"
  }

  return "Erro inesperado"
}
```

## React Query Patterns

### Query Key Centralization
```typescript
// lib/query/queryKeys.ts
export const queryKeys = {
  students: {
    all: ["students"] as const,
    lists: () => ["students", "list"] as const,
    list: (params: Record<string, unknown>) => ["students", "list", params] as const,
    detail: (id: string) => ["students", "detail", id] as const,
  },
} as const
```

### Query Hook Pattern
```typescript
// features/students/hooks/use-students.ts
export function useStudentsQuery(page: number, size: number, search?: string) {
  return useQuery({
    queryKey: queryKeys.students.list({ page, size, search }),
    queryFn: () => studentsApi.list(page, size, search),
    staleTime: 1000 * 60 * 5,
  })
}
```

### Mutation Hook Pattern
```typescript
export function useCreateStudent() {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  return useMutation({
    mutationFn: (data: StudentFormInput) => studentsApi.create(data),
    onSuccess: (created) => {
      toast.success("Aluno criado com sucesso!")
      queryClient.invalidateQueries({ queryKey: queryKeys.students.lists() })
      navigate(`/students/${created.id}`)
    },
    onError: (error) => {
      toast.error(getFriendlyErrorMessage(error))
    },
  })
}
```

### Conditional Query with `enabled`
```typescript
const results = useStudentsQuery(currentPage, pageSize, searchTerm, {
  enabled: variant === "studentsPage",
})
```

### Keep Previous Data for Pagination
```typescript
import { keepPreviousData } from "@tanstack/react-query"

export function useEventsQuery(...) {
  return useQuery({
    queryKey: queryKeys.events.list({ page, size, search }),
    queryFn: () => eventsApi.list(page, size, search),
    placeholderData: keepPreviousData,
  })
}
```

## Form Patterns

Using react-hook-form with zod-resolver:

```typescript
const {
  register,
  handleSubmit,
  setValue,
  watch,
  formState: { errors },
} = useForm<StudentFormInput>({
  resolver: zodResolver(studentInputSchema),
  mode: "onBlur",
})

return (
  <form onSubmit={handleSubmit(onSubmit)}>
    <input {...register("name")} />
    {errors.name && <span>{errors.name.message}</span>}
    {/* ... */}
  </form>
)
```

## UI Components

### Loading and Error States
```tsx
if (isLoading) {
  return <PageLoading message="Carregando alunos..." />
}

if (isError) {
  return <ErrorCard description={getFriendlyErrorMessage(error)} onAction={refetch} />
}
```

### Error Boundary
```tsx
export class ErrorBoundary extends Component<{ children?: ReactNode }, { error: Error | null }> {
  static getDerivedStateFromError(error: Error) {
    return { error }
  }

  render() {
    if (this.state.error) {
      return <ErrorCard title="Ops..." description={this.state.error.message} />
    }
    return this.props.children
  }
}
```

## Import Conventions

Always use `@/` alias, never deep relative imports:

```typescript
// Correct
import { ButtonLink } from "@/components/ui/button"
import { queryKeys } from "@/lib/query/queryKeys"
import { studentsApi } from "@/services/api"

// Avoid
import { ButtonLink } from "../../components/ui/button"
```

Use `import type` for type-only imports:

```typescript
import type { ReactNode } from "react"
import type { StudentResponse } from "@/lib/schemas/student"
```

## Performance Patterns (from Vercel Best Practices)

### Parallel Data Fetching
```typescript
// Use Promise.allSettled for independent queries
const [studentsResult, eventsResult] = await Promise.allSettled([
  studentsApi.list(),
  eventsApi.list(),
])

if (studentsResult.status === "fulfilled") {
  // handle success
} else {
  console.error("Failed to load students:", studentsResult.reason)
}
```

### Memoization
```typescript
// Use useMemo for expensive computations
const sortedData = useMemo(
  () => [...data].sort((a, b) => a.name.localeCompare(b.name)),
  [data]
)

// Use useCallback for stable references
const handleSubmit = useCallback(() => { ... }, [dependencies])
```

### Early Exit Pattern
```typescript
// Return early to avoid nesting
if (isLoading) return <Loading />
if (isError) return <ErrorCard />

// Main content
return <div>...</div>
```
