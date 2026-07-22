import { DespesasOverview } from "@/components/despesas/DespesasOverview";

export default function DespesasPage() {
  return (
    <section className="space-y-6">
      <div className="app-shell-card p-6 md:p-8">
        <p className="text-sm font-semibold uppercase tracking-[0.18em] text-success">Despesas</p>
        <h1 className="mt-2 text-3xl font-bold text-base-content">Gerenciamento de despesas</h1>
        <p className="mt-3 max-w-2xl text-sm text-base-content/65">
          Registre e acompanhe os gastos operacionais da instituição.
        </p>
      </div>

      <DespesasOverview />
    </section>
  );
}
