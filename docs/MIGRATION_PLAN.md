# Kubb Zod + Client Migration Plan

## Goal
Replace `plugin-ts + plugin-zod + plugin-react-query` with `plugin-zod + plugin-client` for cleaner generated output and full control over React Query hooks.

## Generated Output (After)
- `src/gen/schemas/**` — Zod schemas + inferred types
- `src/gen/client/**` — HTTP request functions (Axios-based)
- `src/gen/.kubb/**` — Internal helpers (do not edit)

## Stop Generating
- `src/gen/types/**` (removed)
- `src/gen/hooks/**` (removed)

## Migration Steps

### 1. Update `client/kubb.config.ts`
```ts
import { defineConfig } from '@kubb/core'
import { pluginOas } from '@kubb/plugin-oas'
import { pluginZod } from '@kubb/plugin-zod'
import { pluginClient } from '@kubb/plugin-client'

export default defineConfig({
  input: {
    path: 'http://localhost:8080/v3/api-docs',
  },
  output: {
    path: './src/gen',
    clean: true,
  },
  plugins: [
    pluginOas(),

    pluginZod({
      output: { path: './schemas' },
      inferred: true,
      typed: true,
      group: {
        type: 'tag',
        name: ({ group }) => group.toLowerCase(),
      },
    }),

    pluginClient({
      output: { path: './client' },
      group: {
        type: 'tag',
        name: ({ group }) => group.toLowerCase(),
      },
    }),
  ],
})
```

### 2. Update `client/package.json` Scripts
```json
{
  "scripts": {
    "dev": "vite",
    "build": "tsc -b && vite build",
    "lint": "eslint .",
    "preview": "vite preview",
    "sync": "kubb generate"
  }
}
```

### 3. Reset Generated Folder
```bash
rm -rf client/src/gen/
npm run sync
```

Expected structure:
```
src/gen/
├── client/
│   └── dashboard/
│       └── getDashboardSummary.ts
├── schemas/
│   └── dashboard/
│       └── getDashboardSummarySchema.ts
└── .kubb/
    └── config.ts
```

### 4. Migration Checklist by File

#### `client/src/features/dashboard/query/dashboardQueryKeys.ts`
```ts
export interface DashboardSummaryParams {
  year: number;
  month: number;
}

export const dashboardQueryKeys = {
  all: ["dashboard"] as const,
  summary: (params: DashboardSummaryParams) =>
    ["dashboard", "summary", params] as const,
} as const;
```

#### `client/src/features/dashboard/api/dashboardApi.ts`
```ts
import { getDashboardSummary } from "@/gen/client/dashboard/getDashboardSummary";
import { getDashboardSummaryQueryResponseSchema } from "@/gen/schemas/dashboard/getDashboardSummarySchema";

interface DashboardSummaryParams {
  year: number;
  month: number;
}

function unwrapData<T>(value: T | { data: T }): T {
  if (
    value &&
    typeof value === "object" &&
    "data" in value &&
    (value as { data?: unknown }).data !== undefined
  ) {
    return (value as { data: T }).data;
  }
  return value as T;
}

export async function fetchDashboardSummary(params: DashboardSummaryParams) {
  const raw = await getDashboardSummary(params);
  const data = unwrapData(raw);
  return getDashboardSummaryQueryResponseSchema.parse(data);
}
```

#### `client/src/features/dashboard/query/useDashboardQueries.ts`
```ts
import { useQuery } from "@tanstack/react-query";
import { fetchDashboardSummary } from "@/features/dashboard/api/dashboardApi";
import { dashboardQueryKeys } from "@/features/dashboard/query/dashboardQueryKeys";

interface DashboardSummaryParams {
  year: number;
  month: number;
}

export function useDashboardSummaryQuery(params: DashboardSummaryParams) {
  return useQuery({
    queryKey: dashboardQueryKeys.summary(params),
    queryFn: () => fetchDashboardSummary(params),
    enabled: Boolean(params.year && params.month),
    refetchInterval: 60_000,
  });
}
```

#### `client/src/features/dashboard/DashboardPage.tsx`
```tsx
import { ErrorCard } from "@/components/ui/error-card";
import { PageLoading } from "@/components/ui/page-loading";
import styles from "@/features/dashboard/DashboardPage.module.css";
import { useDashboardSummaryQuery } from "@/features/dashboard/query/useDashboardQueries";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { brl } from "@/lib/utils/formatter";
import { PizzaChart } from "./components/PizzaChart";

function getCurrentYearMonth() {
  const now = new Date();
  return {
    year: now.getFullYear(),
    month: now.getMonth() + 1,
  };
}

export function DashboardPage() {
  const { year, month } = getCurrentYearMonth();

  const { data, isLoading, isError, error, refetch } = useDashboardSummaryQuery({
    year,
    month,
  });

  if (isLoading) {
    return <PageLoading message="Carregando painel..." />;
  }

  if (isError || !data) {
    return (
      <div className={styles.errorWrap}>
        <h1 className="app-text text-3xl font-bold">Painel</h1>
        <ErrorCard
          title="Ops, não foi possível carregar"
          description={getFriendlyErrorMessage(error)}
          onAction={refetch}
        />
      </div>
    );
  }

  return (
    <div className={styles.page}>
      <h1 className="app-text text-3xl font-bold">Painel</h1>

      <div className={styles.kpiGrid}>
        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body gap-2">
            <h2 className="app-kpi-label">Alunos ativos</h2>
            <div className="app-kpi-value">{data.activeStudentsInMonth ?? 0}</div>
          </div>
        </div>

        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body gap-2">
            <h2 className="app-kpi-label">Aulas no mês</h2>
            <div className="app-kpi-value">{data.classesInMonth ?? 0}</div>
          </div>
        </div>

        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body gap-2">
            <h2 className="app-kpi-label">Receita no mês</h2>
            <div className="app-kpi-value">
              {brl.format(data.revenueInMonth ?? 0)}
            </div>
          </div>
        </div>

        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body gap-2">
            <h2 className="app-kpi-label">Custo no mês</h2>
            <div className="app-kpi-value">{brl.format(data.costInMonth ?? 0)}</div>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body">
            <h2 className="card-title text-lg font-semibold">
              Distribuição de Conteúdo
            </h2>
            <p className="text-sm text-base-content/60 mb-4">
              Visualização das aulas por categoria de atendimento.
            </p>
            <PizzaChart data={data.charts ?? []} />
          </div>
        </div>

        <div className="card border border-base-300 bg-base-100 shadow-sm flex items-center justify-center min-h-[300px]">
          <div className="text-center p-8">
            <h3 className="text-base-content/40 font-medium italic">
              Gráfico de Evolução
            </h3>
            <p className="text-xs text-base-content/30 mt-1">Em desenvolvimento</p>
          </div>
        </div>
      </div>
    </div>
  );
}
```

#### `client/src/features/dashboard/components/PizzaChart.tsx`
```tsx
import {
  Legend,
  Pie,
  PieChart,
  ResponsiveContainer,
  Tooltip,
  type PieLabelRenderProps,
} from "recharts";
import { z } from "zod";
import { dashboardSummaryResponseDTOSchema } from "@/gen/schemas/dashboardSummaryResponseDTOSchema";
import { eventContentLabels } from "@/features/events/schemas/eventContentEnum";

type DashboardSummary = z.infer<typeof dashboardSummaryResponseDTOSchema>;
type ClassesByContentDTO = NonNullable<DashboardSummary["charts"]>[number];

const COLORS = [
  "#3b82f6",
  "#10b981",
  "#f59e0b",
  "#ef4444",
  "#8b5cf6",
  "#ec4899",
  "#06b6d4",
];

const RADIAN = Math.PI / 180;

const renderCustomizedLabel = ({
  cx,
  cy,
  midAngle,
  innerRadius,
  outerRadius,
  percent,
}: PieLabelRenderProps) => {
  if (cx == null || cy == null || innerRadius == null || outerRadius == null) {
    return null;
  }

  const radius =
    Number(innerRadius) + (Number(outerRadius) - Number(innerRadius)) * 0.5;
  const x = Number(cx) + radius * Math.cos(-(midAngle ?? 0) * RADIAN);
  const y = Number(cy) + radius * Math.sin(-(midAngle ?? 0) * RADIAN);

  if ((percent ?? 0) < 0.05) return null;

  return (
    <text
      x={x}
      y={y}
      fill="white"
      textAnchor={x > Number(cx) ? "start" : "end"}
      dominantBaseline="central"
      className="text-[10px] font-bold sm:text-xs"
    >
      {`${((percent ?? 0) * 100).toFixed(0)}%`}
    </text>
  );
};

interface PizzaChartProps {
  data: ClassesByContentDTO[];
  isAnimationActive?: boolean;
}

export function PizzaChart({
  data,
  isAnimationActive = true,
}: Readonly<PizzaChartProps>) {
  const chartData = data.map((item, index) => ({
    name:
      eventContentLabels[item.content as keyof typeof eventContentLabels] ||
      item.content,
    value: item.count ?? 0,
    fill: COLORS[index % COLORS.length],
  }));

  if (chartData.length === 0) {
    return (
      <div className="flex h-64 w-full items-center justify-center rounded-lg border border-dashed border-base-300 italic text-base-content/50">
        Nenhuma aula registrada neste período
      </div>
    );
  }

  return (
    <div className="h-80 w-full min-h-80">
      <ResponsiveContainer width="100%" height="100%">
        <PieChart>
          <Pie
            data={chartData}
            cx="50%"
            cy="50%"
            labelLine={false}
            label={renderCustomizedLabel}
            outerRadius={100}
            dataKey="value"
            isAnimationActive={isAnimationActive}
          />
          <Tooltip formatter={(value: number | string) => [`${value}`]} />
          <Legend
            verticalAlign="bottom"
            height={36}
            iconType="circle"
            wrapperStyle={{ paddingTop: "20px" }}
          />
        </PieChart>
      </ResponsiveContainer>
    </div>
  );
}
```

### 5. Post-Migration Validation
- [ ] Backend running on `http://localhost:8080`
- [ ] `npm run sync` succeeds
- [ ] Only `client/` and `schemas/` folders under `client/src/gen/`
- [ ] No imports from `@/gen/**/hooks/` or `@/gen/**/types/`
- [ ] `npm run lint` passes
- [ ] `npm run build` passes
- [ ] Dashboard page loads and renders parsed data

### 6. Import Pattern for Future Features
```ts
// Use generated client function
import { createStudent } from "@/gen/client/student/createStudent"

// Use generated zod schema
import { createStudentMutationRequestSchema } from "@/gen/schemas/student/createStudentSchema"

// Parse and call
const payload = createStudentMutationRequestSchema.parse(formData)
const response = await createStudent(payload)

// Infer type from schema
import type { CreateStudentMutationRequestSchema } from "@/gen/schemas/student/createStudentSchema"
type CreateStudentInput = CreateStudentMutationRequestSchema
```
