import {
  Legend,
  Pie,
  PieChart,
  ResponsiveContainer,
  Tooltip,
  type PieLabelRenderProps,
} from "recharts";

import { type DashboardSummaryResponseDTO } from "@/kubb";
import { EventContentLabels } from "@/lib/shared/eventContentLables";

type ClassesByContentDTO = NonNullable<DashboardSummaryResponseDTO["charts"]>[number];
type ChartDatum = {
  name: string;
  value: number;
  fill: string;
};

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
  const chartData: ChartDatum[] = data.map((item, index) => ({
    name: EventContentLabels[item.content] || item.content,
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
          <Tooltip formatter={(value) => [String(value ?? ""), "Quantidade"]} />
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
