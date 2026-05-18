import { ErrorCard } from "@/components/ui/error-card";
import { brl } from "@/lib/utils/formatter";
import {
  ArrowDownRight,
  ArrowUpRight,
  CircleDollarSign,
  Clock3,
  Wallet,
} from "lucide-react";
import { FinanceKpiCard } from "./FinanceKpiCard";

type FinanceSummarySectionProps = {
  currentBalance: number;
  totalStudentCharged: number;
  totalStudentPending: number;
  totalEmployeePaid: number;
  totalEmployeePending: number;
  totalGeneralExpenses: number;
  pendingGeneralExpenses: number;
  isError: boolean;
  error?: unknown;
};

export function FinanceSummarySection({
  currentBalance,
  totalStudentCharged,
  totalStudentPending,
  totalEmployeePaid,
  totalEmployeePending,
  totalGeneralExpenses,
  pendingGeneralExpenses,
  isError,
  error,
}: Readonly<FinanceSummarySectionProps>) {
  return (
    <section className="rounded-2xl border border-base-300 bg-base-100 p-5 shadow-sm animate-[fade-up_200ms_ease-out_both]">
      <div className="space-y-5">
        <div className="flex flex-col gap-5 lg:flex-row lg:items-start lg:justify-between">
          <div className="space-y-3">
            <div className="flex flex-wrap items-center gap-2">
              <span className="badge badge-primary badge-outline px-3 py-3">
                Resumo institucional
              </span>
            </div>
            <div>
              <h2 className="text-2xl font-bold text-base-content">
                Saldo consolidado da operação
              </h2>
              <p className="mt-1 max-w-3xl text-sm leading-6 text-base-content/60">
                Lucro consolidado da operação reflete nas transações de entrada e saída registradas.
              </p>
            </div>
          </div>

          <div className="rounded-2xl border border-primary/15 bg-linear-to-br from-primary/8 via-base-100 to-base-100 px-4 py-3 shadow-sm">
            <p className="text-[11px] font-bold uppercase tracking-[0.22em] text-base-content/45">
              Saldo consolidado
            </p>
            <p
              className={`mt-2 font-mono text-3xl font-bold ${currentBalance >= 0 ? "text-success" : "text-error"}`}
            >
              {brl.format(currentBalance)}
            </p>
            <p className="mt-2 text-xs text-base-content/55">
              Total recebido dos alunos menos repasses pagos e despesas gerais
              baixadas.
            </p>
          </div>
        </div>

        {isError ? (
          <ErrorCard
            title="Nao foi possivel carregar o resumo financeiro"
            error={error}
          />
        ) : (
          <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-3">
            {/* Alunos */}
            <div className="rounded-xl border border-base-200 bg-base-100 p-4">
              <span className="badge badge-sm badge-outline mb-3">Alunos</span>
              <div className="space-y-3">
                <FinanceKpiCard
                  title="Recebido"
                  value={brl.format(totalStudentCharged)}
                  tone="success"
                  icon={<ArrowDownRight className="h-5 w-5" />}
                />
                <FinanceKpiCard
                  title="Pendente"
                  value={brl.format(totalStudentPending)}
                  tone="warning"
                  icon={<Wallet className="h-5 w-5" />}
                />
              </div>
            </div>

            {/* Colaboradores */}
            <div className="rounded-xl border border-base-200 bg-base-100 p-4">
              <span className="badge badge-sm badge-outline mb-3">Colaboradores</span>
              <div className="space-y-3">
                <FinanceKpiCard
                  title="Pago"
                  value={brl.format(totalEmployeePaid)}
                  tone="success"
                  icon={<ArrowUpRight className="h-5 w-5" />}
                />
                <FinanceKpiCard
                  title="Pendente"
                  value={brl.format(totalEmployeePending)}
                  tone="warning"
                  icon={<CircleDollarSign className="h-5 w-5" />}
                />
              </div>
            </div>

            {/* Despesas */}
            <div className="rounded-xl border border-base-200 bg-base-100 p-4">
              <span className="badge badge-sm badge-outline mb-3">Despesas</span>
              <div className="space-y-3">
                <FinanceKpiCard
                  title="Pagas"
                  value={brl.format(totalGeneralExpenses)}
                  tone="success"
                  icon={<ArrowUpRight className="h-5 w-5" />}
                />
                <FinanceKpiCard
                  title="Pendentes"
                  value={brl.format(pendingGeneralExpenses)}
                  tone="warning"
                  icon={<Clock3 className="h-5 w-5" />}
                />
              </div>
            </div>
          </div>
        )}
      </div>
    </section>
  );
}
