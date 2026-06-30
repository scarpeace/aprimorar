import { AtendimentosOverview } from "@/components/atendimentos/AtendimentosOverview";

export default function AtendimentosPage() {
  return (
    <section className="space-y-6">
      <div className="app-shell-card p-6 md:p-8">
        <p className="text-sm font-semibold uppercase tracking-[0.18em] text-success">Atendimentos</p>
        <h1 className="mt-2 text-3xl font-bold text-base-content">Listagem de atendimentos</h1>
        <p className="mt-3 max-w-2xl text-sm text-base-content/65">
          Navegação mensal com busca e filtros sobre o contrato atual da API.
        </p>
      </div>

      <AtendimentosOverview />
    </section>
  );
}
