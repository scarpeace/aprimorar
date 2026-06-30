import { ColaboradoresOverview } from "@/components/colaboradores/ColaboradoresOverview";

export default function ColaboradoresPage() {
  return (
    <section className="space-y-6">
      <div className="app-shell-card p-6 md:p-8">
        <p className="text-sm font-semibold uppercase tracking-[0.18em] text-success">Colaboradores</p>
        <h1 className="mt-2 text-3xl font-bold text-base-content">Listagem de colaboradores</h1>
        <p className="mt-3 max-w-2xl text-sm text-base-content/65">
          Listagem paginada com criação, edição e acesso ao detalhe dos colaboradores.
        </p>
      </div>

      <ColaboradoresOverview />
    </section>
  );
}
