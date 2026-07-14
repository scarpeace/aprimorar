import { AlunosKpis } from "@/components/alunos/AlunosKpis";
import { AlunosOverview } from "@/components/alunos/AlunosOverview";
import { ResponsaveisOverview } from "@/components/responsaveis/ResponsaveisOverview";

export default function AlunosPage() {
  return (
    <section className="flex flex-col gap-6">
      <div className="app-shell-card flex flex-col gap-5 p-6 md:p-8 lg:flex-row lg:items-center lg:justify-between">
        <div>
          <p className="text-sm font-semibold uppercase tracking-[0.18em] text-success">Alunos</p>
          <h1 className="mt-2 text-3xl font-bold text-base-content">Alunos e responsáveis</h1>
          <p className="mt-3 max-w-2xl text-sm text-base-content/65">Acompanhe e gerencie a base de alunos e responsáveis.</p>
        </div>

        <AlunosKpis />
      </div>

      <div className="grid gap-6 xl:grid-cols-2">
        <AlunosOverview />
        <ResponsaveisOverview />
      </div>
    </section>
  );
}
