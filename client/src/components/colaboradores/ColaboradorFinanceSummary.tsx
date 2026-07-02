"use client";

import { keepPreviousData } from "@tanstack/react-query";
import { useGetResumoFinanceiroColaborador } from "@/lib/api/generated/hooks/atendimento/useGetResumoFinanceiroColaborador";
import { ErrorCard } from "@/components/ui/ErrorCard";
import { getFriendlyErrorMessage } from "@/lib/api/client";
import { brl } from "@/lib/utils/formatter";

type ColaboradorFinanceSummaryProps = {
  colaboradorId: string;
  anoMes: string;
};

function SummaryCard({ label, value, tone }: Readonly<{ label: string; value?: number; tone: "success" | "warning" }>) {
  return (
    <div className="rounded-2xl border border-base-300 bg-base-100 p-5 shadow-sm">
      <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">{label}</p>
      <p className={`mt-3 text-3xl font-bold ${tone === "success" ? "text-success" : "text-warning"}`}>
        {brl.format(value ?? 0)}
      </p>
    </div>
  );
}

export function ColaboradorFinanceSummary({ colaboradorId, anoMes }: Readonly<ColaboradorFinanceSummaryProps>) {
  const resumo = useGetResumoFinanceiroColaborador(
    colaboradorId,
    { anoMes },
    {
      query: {
        placeholderData: keepPreviousData,
      },
    },
  );

  if (resumo.error) {
    return (
      <ErrorCard
        title="Não foi possível carregar o resumo financeiro"
        description={getFriendlyErrorMessage(resumo.error)}
        error={resumo.error}
      />
    );
  }

  return (
    <section className="rounded-2xl border border-base-300 bg-base-100 p-6 shadow-sm">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-start lg:justify-between">
        <div>
          <h2 className="text-xl font-bold text-base-content">Resumo financeiro</h2>
          <p className="mt-2 text-sm text-base-content/65">Acompanhe o repasse pago e o repasse pendente do período selecionado.</p>
        </div>

        <div className="grid gap-3 sm:grid-cols-2">
          <SummaryCard label="Repasse pago" value={resumo.data?.totalPago} tone="success" />
          <SummaryCard label="Repasse pendente" value={resumo.data?.totalPendente} tone="warning" />
        </div>
      </div>
    </section>
  );
}
