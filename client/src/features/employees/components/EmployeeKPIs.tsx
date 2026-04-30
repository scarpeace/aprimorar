import { KpiCard } from "@/components/ui/kpi-card";
import { SectionCard } from "@/components/ui/section-card";
import { useGetEmployeeMonthlySummary } from "@/kubb";
import { brl } from "@/lib/utils/formatter";
import { useSearchParams } from "react-router-dom";

interface EmployeeKPIsProps {
  employeeId: string;
}

export function EmployeeKPIs({ employeeId }: EmployeeKPIsProps) {
  const [searchParams] = useSearchParams();
  const month = parseInt(
    searchParams.get("month") ?? String(new Date().getMonth() + 1),
  );
  const year = parseInt(
    searchParams.get("year") ?? String(new Date().getFullYear()),
  );

  const summaryQuery = useGetEmployeeMonthlySummary(employeeId, {
    month,
    year,
  });

  return (
    <SectionCard
      title="Resumo mensal"
      description="Resumo dos atendimentos do colaborador no mês"
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
            value={summaryQuery.data?.totalEvents ?? 0}
          />
          <KpiCard
            className="grow"
            label="Total Pago"
            value={brl.format(summaryQuery.data?.totalPaid ?? 0)}
          />
          <KpiCard
            className="grow"
            label="Total Pendente"
            value={brl.format(summaryQuery.data?.totalUnpaid ?? 0)}
          />
        </div>
      )}
    </SectionCard>
  );
}
