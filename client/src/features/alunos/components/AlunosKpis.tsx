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
    <div className="grid grid-cols-2 gap-5 lg:min-w-72">
      <div className="space-y-1">
        <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">Cadastrados</p>
        <p className="text-2xl font-bold text-base-content">{alunosKpis.data?.totalAlunos ?? 0}</p>
      </div>

      <div className="space-y-1">
        <p className="text-xs font-semibold uppercase tracking-wider text-base-content/50">Ativos</p>
        <p className="text-2xl font-bold text-success">{alunosKpis.data?.totalAlunosAtivos ?? 0}</p>
      </div>
    </div>
  );
}
