import { EventContentLabels } from "@/features/appointments/lib/eventContentLabels.ts";
import { APPOINTMENT_CONTENT_COLORS } from "@/features/appointments/lib/appointment-content-colors";

type DashboardChartDatum = {
  content?: string;
  count?: number;
};

type AtendimentoContentLegendProps = {
  distribution?: DashboardChartDatum[];
  totalAppointments?: number;
};

export function AtendimentoContentLegend({
  distribution,
  totalAppointments = 0,
}: Readonly<AtendimentoContentLegendProps>) {
  return (
    <div className="mb-4 rounded-2xl border border-base-300 bg-base-200/40 px-4 py-3">
      <p className="mb-2 text-xs font-bold uppercase tracking-[0.16em] text-base-content/50">
        Total de atendimentos: {totalAppointments}
      </p>
      <div className="flex justify-between gap-3">
        {Object.entries(APPOINTMENT_CONTENT_COLORS).map(([content, color]) => {
          const count = distribution?.find((item) => item.content === content)?.count ?? 0;
          const palette = color as { backgroundColor: string };

          return (
            <div key={content} className="flex items-center gap-2 text-sm font-medium text-base-content/75">
              <span
                className="h-2.5 w-2.5 rounded-full"
                style={{ backgroundColor: palette.backgroundColor }}
              />
              <span>{EventContentLabels[content] ?? content} : {count}</span>
            </div>
          );
        })}
      </div>
    </div>
  );
}
