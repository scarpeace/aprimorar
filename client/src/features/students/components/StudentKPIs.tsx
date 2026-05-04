import { KpiCard } from "@/components/ui/kpi-card";
import { SectionCard } from "@/components/ui/section-card";
import { useGetStudentSummary } from "@/kubb";
import { brl } from "@/lib/utils/formatter";
import { CalendarCheck, CircleDollarSign, Wallet } from "lucide-react";
import { useSearchParams } from "react-router-dom";
import { memo } from "react";

interface StudentKPIsProps {
  studentId: string;
}

export const StudentKPIs = memo(function StudentKPIs({ studentId }: StudentKPIsProps) {
  const [searchParams] = useSearchParams();

  const startDate = searchParams.get("startDate") || undefined;
  const endDate = searchParams.get("endDate") || undefined;

  const summaryQuery = useGetStudentSummary(studentId, {
    startDate,
    endDate,
  });

  return (
    <>
      {summaryQuery.isPending ? (
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
          <div className="h-24 sm:h-32 w-full animate-pulse rounded-xl bg-base-200" />
          <div className="h-24 sm:h-32 w-full animate-pulse rounded-xl bg-base-200" />
          <div className="h-24 sm:h-32 w-full animate-pulse rounded-xl bg-base-200" />
        </div>
      ) : summaryQuery.isError ? (
        <div className="alert alert-error shadow-sm">
          <span className="text-sm font-medium">Erro ao carregar o resumo do aluno.</span>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
          <KpiCard
            className="animate-[fade-up_400ms_ease-out_both]"
            label="Total Atendimentos"
            value={summaryQuery.data?.totalEvents ?? 0}
            Icon={CalendarCheck}
          />
          <KpiCard
            className="animate-[fade-up_500ms_ease-out_both]"
            label="Total Cobrado"
            value={brl.format(summaryQuery.data?.totalCharged ?? 0)}
            Icon={CircleDollarSign}
          />
          <KpiCard
            className="animate-[fade-up_600ms_ease-out_both]"
            label="Total Pendente"
            value={brl.format(summaryQuery.data?.totalPending ?? 0)}
            Icon={Wallet}
          />
        </div>
      )}
    </>
  );
});
