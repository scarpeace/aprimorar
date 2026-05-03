import { KpiCard } from "@/components/ui/kpi-card";
import { SectionCard } from "@/components/ui/section-card";
import { useGetStudentSummary } from "@/kubb";
import { brl } from "@/lib/utils/formatter";
import { useSearchParams } from "react-router-dom";

interface StudentKPIsProps {
  studentId: string;
}

export function StudentKPIs({ studentId }: StudentKPIsProps) {
  const [searchParams] = useSearchParams();

  const startDate = searchParams.get("startDate") || undefined;
  const endDate = searchParams.get("endDate") || undefined;

  const summaryQuery = useGetStudentSummary(studentId, {
    startDate,
    endDate,
  });

  return (
    <SectionCard
      title="Resumo"
      description={
        startDate && endDate
          ? `Resumo financeiro e de atendimentos do aluno no período selecionado`
          : `Resumo financeiro e de atendimentos do aluno (Todo o período)`
      }
    >
      {summaryQuery.isPending ? (
        <div className="flex flex-col gap-3">
          <div className="h-24 w-full animate-pulse rounded-lg bg-base-200" />
        </div>
      ) : summaryQuery.isError ? (
        <div className="alert alert-error">
          <span className="text-sm">Erro ao carregar o resumo do aluno.</span>
        </div>
      ) : (
        <div className="flex justify-between gap-3">
          <KpiCard
            label="Total de atendimentos"
            value={summaryQuery.data?.totalEvents ?? 0}
          />
          <KpiCard
            className="grow"
            label="Total Cobrado"
            value={brl.format(summaryQuery.data?.totalCharged ?? 0)}
          />
          <KpiCard
            className="grow"
            label="Total Pendente"
            value={brl.format(summaryQuery.data?.totalPending ?? 0)}
          />
        </div>
      )}
    </SectionCard>
  );
}
