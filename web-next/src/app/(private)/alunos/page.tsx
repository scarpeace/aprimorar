import { AlunosOverview } from "@/components/alunos/AlunosOverview";

export default function AlunosPage() {
  return (
    <section className="space-y-6">
      <div className="app-shell-card p-6 md:p-8">
        <p className="text-sm font-semibold uppercase tracking-[0.18em] text-success">Alunos</p>
        <h1 className="mt-2 text-3xl font-bold text-base-content">Listagem de alunos</h1>
        <p className="mt-3 max-w-2xl text-sm text-base-content/65">
          Primeira tela real de alunos no `web-next`, sem CRUD ainda, só consulta paginada.
        </p>
      </div>

      <AlunosOverview />
    </section>
  );
}
