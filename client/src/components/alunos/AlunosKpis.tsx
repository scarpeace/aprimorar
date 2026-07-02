"use client";

import { useGetAlunosKpis } from "@/lib/api/generated/hooks/aluno/useGetAlunosKpis";
import { ErrorCard } from "@/components/ui/ErrorCard";

export function AlunosKpis() {
  const alunosKpis = useGetAlunosKpis();

  if (alunosKpis.error) {
    return (
      <ErrorCard
        title="Não foi possível carregar os KPIs de alunos"
        description="As listagens podem continuar funcionando, mas o resumo superior não ficou disponível."
        error={alunosKpis.error}
      />
    );
  }

  return (
    <section className="app-shell-card p-6">
      <h2 className="text-xl font-bold text-base-content">Indicadores</h2>

      <div className="mt-6 grid gap-6 md:grid-cols-2 md:gap-0 md:divide-x md:divide-base-300">
        <div className="space-y-1 md:pr-6">
          <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">Alunos cadastrados</p>
          <p className="text-3xl font-bold text-base-content">{alunosKpis.data?.totalAlunos ?? 0}</p>
        </div>

        <div className="space-y-1 md:pl-6">
          <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">Alunos ativos</p>
          <p className="text-3xl font-bold text-success">{alunosKpis.data?.totalAlunosAtivos ?? 0}</p>
        </div>
      </div>
    </section>
  );
}
