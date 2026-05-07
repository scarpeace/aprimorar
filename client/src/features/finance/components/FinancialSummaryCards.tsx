import { useGetFinanceSummary } from "@/kubb/hooks/finance/useGetFinanceSummary";
import { KpiCard } from "@/components/ui/kpi-card";
import { brl } from "@/lib/utils/formatter";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { ErrorCard } from "@/components/ui/error-card";
import { CircleDollarSign, TrendingDown, TrendingUp, Wallet, Landmark } from "lucide-react";

export function FinancialSummaryCards() {
  const { data: summary, isLoading, isError } = useGetFinanceSummary();

  if (isLoading) {
    return (
      <div className="flex h-32 items-center justify-center">
        <LoadingSpinner />
      </div>
    )
  }

  if (isError) {
    return <ErrorCard title="Erro ao carregar resumo financeiro" />
  }

  return (
    <div className="grid grid-cols-1 gap-4 md:grid-cols-3 lg:grid-cols-5">
      <KpiCard
        label="Total Recebido"
        value={<span className="text-success">{brl.format(summary?.totalIncome ?? 0)}</span>}
        Icon={TrendingUp}
      />
      <KpiCard
        label="Total a Receber"
        value={<span className="text-warning">{brl.format(summary?.totalIncomePending ?? 0)}</span>}
        Icon={Wallet}
      />
      <KpiCard
        label="Total Pago"
        value={
          <span className="text-error">
            {brl.format((summary?.totalExpenseTeacher ?? 0) + (summary?.totalGeneralExpenses ?? 0))}
          </span>
        }
        Icon={TrendingDown}
      />
      <KpiCard
        label="Total a Pagar"
        value={<span className="text-warning">{brl.format(summary?.totalExpenseTeacherPending ?? 0)}</span>}
        Icon={CircleDollarSign}
      />
      <KpiCard
        label="Saldo"
        value={
          <span className={summary?.balance && summary.balance >= 0 ? "text-success" : "text-error"}>
            {brl.format(summary?.balance ?? 0)}
          </span>
        }
        Icon={Landmark}
      />
    </div>
  );
}
