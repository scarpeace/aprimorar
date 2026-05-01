import { KpiCard } from "@/components/ui/kpi-card";
import { SectionCard } from "@/components/ui/section-card";
import { useGetStudentMonthlySummary } from "@/kubb";
import { brl } from "@/lib/utils/formatter";
import { useSearchParams } from "react-router-dom";

interface StudentKPIsProps {
  studentId: string;
}

export function StudentKPIs({ studentId }: StudentKPIsProps) {
  const [searchParams] = useSearchParams();
  
  const startDateStr = searchParams.get("startDate");
  const dateToUse = startDateStr ? new Date(startDateStr) : new Date();
  const month = dateToUse.getMonth() + 1;
  const year = dateToUse.getFullYear();

  const summaryQuery = useGetStudentMonthlySummary(studentId, {
    month,
    year,
  });

  return (
    <SectionCard
      title="Resumo mensal"
      description={`Resumo financeiro e de atendimentos do aluno (${month}/${year})`}
    >
      {summaryQuery.isPending ? (
        <div className="flex flex-col gap-3">
          <div className="h-24 w-full animate-pulse rounded-lg bg-base-200" />
        </div>
      ) : summaryQuery.isError ? (
        <div className="alert alert-error">
          <span className="text-sm">Erro ao carregar o resumo mensal.</span>
        </div>
      ) : (
        <div className="flex justify-between gap-3">
          <KpiCard
            label="Total de atendimentos"
            value={summaryQuery.data?.totalEventsInPeriod ?? 0}
          />
          <KpiCard
            className="grow"
            label="Total Cobrado"
            value={brl.format(summaryQuery.data?.totalChargedInPeriod ?? 0)}
          />
          <KpiCard
            className="grow"
            label="Total Pendente"
            value={brl.format(summaryQuery.data?.totalPendingInPeriod ?? 0)}
          />
        </div>
      )}
    </SectionCard>
  );
}
