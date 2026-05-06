import { KpiCard } from "@/components/ui/kpi-card";
import { SectionCard } from "@/components/ui/section-card";
import { useGetStudentSummary } from "@/kubb";
import { brl } from "@/lib/utils/formatter";
import { CalendarCheck, CircleDollarSign, Wallet } from "lucide-react";
import { useSearchParams } from "react-router-dom";
import { memo } from "react";
import { ErrorCard } from "@/components/ui/error-card";

interface StudentKPIsProps {
  studentId: string;
}

export const StudentKPIs = memo(function StudentKPIs({ studentId }: StudentKPIsProps) {
  const [searchParams] = useSearchParams();

  const startDate = searchParams.get("startDate") || undefined;
  const endDate = searchParams.get("endDate") || undefined;

  const studentSummaryQuery = useGetStudentSummary(studentId, {
    startDate,
    endDate,
  });

  if (studentSummaryQuery.error) {
    return <ErrorCard title="Erro ao carregar detalhes do colaborador" error={studentSummaryQuery.error} />;
  }

  if (studentSummaryQuery.isPending || !studentSummaryQuery.data) {
    return (
      <SectionCard title="Colaborador" description="Dados do Colaborador">
        <div className="h-48 w-full animate-pulse rounded-lg bg-base-300/50" />
      </SectionCard>
    );
  }

  return (
    <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
      <KpiCard
        className="animate-[fade-up_400ms_ease-out_both]"
        label="Total Atendimentos"
        value={studentSummaryQuery.data?.totalEvents ?? 0}
        Icon={CalendarCheck}
      />
      <KpiCard
        className="animate-[fade-up_500ms_ease-out_both]"
        label="Total Cobrado"
        value={brl.format(studentSummaryQuery.data?.totalCharged ?? 0)}
        Icon={CircleDollarSign}
      />
      <KpiCard
        className="animate-[fade-up_600ms_ease-out_both]"
        label="Total Pendente"
        value={brl.format(studentSummaryQuery.data?.totalPending ?? 0)}
        Icon={Wallet}
      />
    </div>
  );
});
