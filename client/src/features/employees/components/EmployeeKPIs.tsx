import { ErrorCard } from "@/components/ui/error-card";
import { KpiCard } from "@/components/ui/kpi-card";
import { SectionCard } from "@/components/ui/section-card";
import { useGetEmployeeSummary } from "@/kubb";
import { brl } from "@/lib/utils/formatter";
import { CalendarCheck, CircleDollarSign, Wallet } from "lucide-react";
import { useSearchParams } from "react-router-dom";

interface EmployeeKPIsProps {
  employeeId: string;
}

export function EmployeeKPIs({ employeeId }: EmployeeKPIsProps) {
  const [searchParams] = useSearchParams();
  const startDate = searchParams.get("startDate") || undefined;
  const endDate = searchParams.get("endDate") || undefined;

  const employeeSummaryQuery = useGetEmployeeSummary(employeeId, {
    startDate,
    endDate,
  });

  if (employeeSummaryQuery.error) {
    return <ErrorCard title="Erro ao carregar detalhes do colaborador" error={employeeSummaryQuery.error} />;
  }

  if (employeeSummaryQuery.isPending || !employeeSummaryQuery.data) {
    return (
      <SectionCard title="Colaborador" description="Dados do Colaborador">
        <div className="h-48 w-full animate-pulse rounded-lg bg-base-300/50" />
      </SectionCard>
    );
  }

  return (

      <div className="flex justify-between gap-3">
        <KpiCard
          label="Total de atendimentos"
          value={employeeSummaryQuery.data?.totalEvents ?? 0} Icon={CalendarCheck}        />
        <KpiCard
          className="grow"
          label="Total Pago"
          value={brl.format(employeeSummaryQuery.data?.totalPaid ?? 0)} Icon={CircleDollarSign}        />
        <KpiCard
          className="grow"
          label="Total Pendente"
          value={brl.format(employeeSummaryQuery.data?.totalUnpaid ?? 0)} Icon={Wallet}        />
      </div>
  );
}
