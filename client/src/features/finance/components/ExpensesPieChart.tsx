import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import type { DespesaResponseDTO } from "@/kubb";
import { brl } from "@/lib/utils/formatter";
import {
  Legend,
  Pie,
  PieChart,
  ResponsiveContainer,
  Tooltip,
  type PieLabelRenderProps,
} from "recharts";
import {
  EXPENSE_CATEGORY_LABEL,
  EXPENSE_CATEGORY_ORDER,
} from "../lib/expense-category-labels";

const EXPENSE_CATEGORY_COLORS: Record<string, string> = {
  CONTAS: "#475569",
  ADMINISTRATIVO: "#0f766e",
  DESPENSA: "#be123c",
  MANUTENCAO: "#d97706",
  SERVICOS: "#7c3aed",
  MATERIAIS: "#0891b2",
  ASSINATURAS: "#047857",
};

const FALLBACK_COLORS = [
  "#475569",
  "#0f766e",
  "#be123c",
  "#d97706",
  "#7c3aed",
  "#0891b2",
  "#047857",
];

const RADIAN = Math.PI / 180;

type ChartDatum = {
  name: string;
  value: number;
  fill: string;
};

type ExpensesPieChartProps = {
  expenses?: DespesaResponseDTO[];
  isPending: boolean;
  error: unknown;
};

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

  if ((percent ?? 0) < 0.05) {
    return null;
  }

  const radius =
    Number(innerRadius) + (Number(outerRadius) - Number(innerRadius)) * 0.5;
  const x = Number(cx) + radius * Math.cos(-(midAngle ?? 0) * RADIAN);
  const y = Number(cy) + radius * Math.sin(-(midAngle ?? 0) * RADIAN);

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

export function ExpensesPieChart({
  expenses,
  isPending,
  error,
}: Readonly<ExpensesPieChartProps>) {
  const totals = (expenses ?? []).reduce<Record<string, number>>(
    (accumulator, expense) => {
      const category = expense.category ?? "SEM_CATEGORIA";
      accumulator[category] =
        (accumulator[category] ?? 0) + (expense.amount ?? 0);
      return accumulator;
    },
    {},
  );

  const orderedCategories = [
    ...EXPENSE_CATEGORY_ORDER.filter((category) => (totals[category] ?? 0) > 0),
    ...Object.keys(totals).filter(
      (category) => !EXPENSE_CATEGORY_ORDER.includes(category),
    ),
  ];

  const chartData: ChartDatum[] = orderedCategories
    .map((category, index) => ({
      name: EXPENSE_CATEGORY_LABEL[category] ?? "Sem categoria",
      value: totals[category] ?? 0,
      fill:
        EXPENSE_CATEGORY_COLORS[category] ??
        FALLBACK_COLORS[index % FALLBACK_COLORS.length],
    }))
    .filter((item) => item.value > 0);

  return (
    <section className="flex h-full min-h-120 flex-col rounded-2xl border border-base-300 bg-base-100 p-5 shadow-lg animate-[fade-up_240ms_ease-out_both]">
      <div className="mb-4">
        <div className="flex flex-wrap items-center gap-2">
          <span className="badge badge-secondary badge-outline px-3 py-3">
            Gastos por Categoria
          </span>
        </div>
        <h3 className="mt-3 text-xl font-bold text-base-content">
          Distribuicao por categoria
        </h3>
        <p className="mt-1 text-sm leading-6 text-base-content/60">
          Participacao de cada categoria no total de despesas.
        </p>
      </div>

      {error ? (
        <ErrorCard
          title="Nao foi possivel carregar o grafico de despesas"
          error={error}
        />
      ) : null}

      {!error && isPending ? (
        <div className="flex flex-1 items-center justify-center">
          <LoadingSpinner text="Carregando grafico de despesas..." />
        </div>
      ) : null}

      {!error && !isPending && chartData.length === 0 ? (
        <div className="flex flex-1 items-center justify-center rounded-2xl border border-dashed border-base-300 px-6 text-center text-sm italic text-base-content/50">
          Nenhuma despesa registrada neste periodo
        </div>
      ) : null}

      {!error && !isPending && chartData.length > 0 ? (
        <div className="min-h-80 flex-1">
          <ResponsiveContainer width="100%" height="100%">
            <PieChart>
              <Pie
                data={chartData}
                cx="50%"
                cy="46%"
                labelLine={false}
                label={renderCustomizedLabel}
                outerRadius={96}
                dataKey="value"
              />
              <Tooltip
                formatter={(value) => [brl.format(Number(value ?? 0)), "Valor"]}
              />
              <Legend
                verticalAlign="bottom"
                height={56}
                iconType="circle"
                wrapperStyle={{ paddingTop: "16px" }}
              />
            </PieChart>
          </ResponsiveContainer>
        </div>
      ) : null}
    </section>
  );
}
