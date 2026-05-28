import { KpiCard } from "@/components/ui/kpi-card";
import { brl } from "@/lib/utils/formatter";
import { PiggyBank } from "lucide-react";

type FinanceiroSummarySectionProps = {
  currentBalance: number;
};

export function FinanceiroSummarySection({
  currentBalance,
}: Readonly<FinanceiroSummarySectionProps>) {
  return (
    <section className="rounded-2xl border border-base-300 bg-base-100 p-5 shadow-sm animate-[fade-up_200ms_ease-out_both]">
      <div className="flex flex-col gap-5 lg:flex-row lg:items-center lg:justify-between">
        <div className="flex flex-col items-start gap-2">
          <span className="badge badge-primary badge-outline px-3 py-3">
            Resumo institucional
          </span>
          <h2 className="text-2xl font-bold text-base-content">
            Saldo consolidado da operação
          </h2>
          <p className="mt-1 max-w-3xl text-sm leading-6 text-base-content/60">
            Total recebido dos alunos menos repasses pagos a colaboradores e
            despesas gerais.
          </p>
        </div>

        <KpiCard
          label="Lucro"
          value={
            <p className={`mt-2 text-3xl font-bold ${currentBalance >= 0 ? "text-success" : "text-error"}`}>
              {brl.format(currentBalance)}
            </p>
          }
          Icon={PiggyBank}
          className={`bg-linear-to-br from-warning/10 via-base-100 to-base-100`}
        />
      </div>
    </section>
  );
}
