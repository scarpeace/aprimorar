import { useGetFinanceSummary } from "@/kubb/hooks/finance/useGetFinanceSummary"
import { KpiCard } from "@/components/ui/kpi-card"
import { brl } from "@/lib/utils/formatter"
import { LoadingSpinner } from "@/components/ui/loading-spinner"
import { ErrorCard } from "@/components/ui/error-card"

export function FinancialSummaryCards() {
  const { data: summary, isLoading, isError, refetch } = useGetFinanceSummary()

  if (isLoading) {
    return (
      <div className="flex h-32 items-center justify-center">
        <LoadingSpinner />
      </div>
    )
  }

  if (isError) {
    return <ErrorCard message="Erro ao carregar resumo financeiro" onRetry={refetch} />
  }

  return (
    <div className="grid grid-cols-1 gap-4 md:grid-cols-3 lg:grid-cols-5">
      <KpiCard
        label="Total Recebido"
        value={<span className="text-success">{brl.format(summary?.totalIncome ?? 0)}</span>}
      />
      <KpiCard
        label="Total a Receber"
        value={<span className="text-warning">{brl.format(summary?.totalIncomePending ?? 0)}</span>}
      />
      <KpiCard
        label="Total Pago"
        value={
          <span className="text-error">
            {brl.format((summary?.totalExpenseTeacher ?? 0) + (summary?.totalGeneralExpenses ?? 0))}
          </span>
        }
      />
      <KpiCard
        label="Total a Pagar"
        value={<span className="text-warning">{brl.format(summary?.totalExpenseTeacherPending ?? 0)}</span>}
      />
      <KpiCard
        label="Saldo"
        value={
          <span className={summary?.balance && summary.balance >= 0 ? "text-success" : "text-error"}>
            {brl.format(summary?.balance ?? 0)}
          </span>
        }
      />
    </div>
  )
}
