import { DashboardOverview } from "@/components/dashboard/DashboardOverview";

export default function DashboardPage() {
  return (
    <section className="space-y-6">
      <div className="app-shell-card p-6 md:p-8">
        <p className="text-sm font-semibold uppercase tracking-[0.18em] text-success">Dashboard</p>
        <h1 className="mt-2 text-3xl font-bold text-base-content">Visão geral da operação</h1>
        <p className="mt-3 max-w-2xl text-sm text-base-content/65">
          Resumo da operação e calendário de atendimentos.
        </p>
      </div>

      <DashboardOverview />
    </section>
  );
}
