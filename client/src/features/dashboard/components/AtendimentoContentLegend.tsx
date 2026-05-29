import { useMemo } from "react";
import { EventContentLabels } from "@/features/atendimentos/lib/eventContentLabels";
import { APPOINTMENT_CONTENT_COLORS } from "@/features/atendimentos/lib/appointment-content-colors";
import { useGetAtendimentosContentReport } from "@/kubb";


type AtendimentoContentLegendProps = {
  startDate: Date;
  endDate: Date;
};

export function AtendimentoContentLegend({
  startDate,
  endDate,
}: Readonly<AtendimentoContentLegendProps>) {
  const { data: report, isLoading, isError } = useGetAtendimentosContentReport({
    startDate: startDate.toISOString(),
    endDate: endDate.toISOString(),
  });

  const countByContent = useMemo<Record<string, number>>(
    () => ({
      AULA: report?.totalAulas ?? 0,
      MENTORIA: report?.totalMentoria ?? 0,
      TERAPIA: report?.totalTerapia ?? 0,
      ORIENTACAO_VOCACIONAL: report?.totalOV ?? 0,
      ENEM: report?.totalENEM ?? 0,
      PAS: report?.totalPAS ?? 0,
      OUTRO: report?.totalOutros ?? 0,
    }),
    [report],
  );

  const totalAppointments = useMemo(
    () => Object.values(countByContent).reduce((acc, count) => acc + count, 0),
    [countByContent],
  );

  return (
    <div className="mb-4 rounded-2xl border border-base-300 bg-base-200/40 px-4 py-3">
      <p className="mb-2 text-xs font-bold uppercase tracking-[0.16em] text-base-content/50">
        Total de atendimentos: {totalAppointments}
      </p>
      {isLoading ? (
        <p className="text-sm text-base-content/60">Carregando distribuição...</p>
      ) : isError ? (
        <p className="text-sm text-error">Não foi possível carregar a distribuição.</p>
      ) : (
        <div className="flex flex-wrap gap-3">
          {Object.entries(APPOINTMENT_CONTENT_COLORS).map(([content, color]) => (
            <div
              key={content}
              className="flex items-center gap-2 rounded-lg border border-base-300 px-2 py-1 text-sm font-medium text-base-content/75"
            >
              <span
                className="h-2.5 w-2.5 rounded-full"
                style={{ backgroundColor: color.backgroundColor }}
              />
              <span>
                {EventContentLabels[content] ?? content} : {countByContent[content] ?? 0}
              </span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
