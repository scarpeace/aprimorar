import { tipoAtendimentoLabels } from "@/features/atendimentos/lib/tipo-atendimento-labels";
import { CORES_TIPO_ATENDIMENTO } from "@/features/atendimentos/lib/cores-tipo-atendimento";
import { useGetAtendimentosReport } from "@/kubb";

export function AtendimentoContentLegend() {
  const anoMes = new Date().toISOString().slice(0, 7);
  const realtorioAtendimentos = useGetAtendimentosReport({ anoMes });
  console.log(realtorioAtendimentos.data)

  const itens = [
    ["AULA", realtorioAtendimentos.data?.totalAulas],
    ["ENEM", realtorioAtendimentos.data?.totalENEM],
    ["PAS", realtorioAtendimentos.data?.totalPAS],
    ["MENTORIA", realtorioAtendimentos.data?.totalMentoria],
    ["TERAPIA", realtorioAtendimentos.data?.totalTerapia],
    ["OUTRO", realtorioAtendimentos.data?.totalOutros],
  ] as const;

  return (
    <div className="mb-4 rounded-2xl border border-base-300 bg-base-200/40 px-4 py-3">
      <p className="mb-2 text-xs font-bold uppercase tracking-[0.16em] text-base-content/50">
        Total de atendimentos : {realtorioAtendimentos.data?.totalAtendimentos}
      </p>

      <div className="flex flex-wrap gap-3">
        {itens.map(([tipo, total]) => (
          <div
            key={tipo}
            className="flex items-center gap-2 rounded-lg border border-base-300 px-2 py-1 text-sm font-medium text-base-content/75"
          >
            <span className="h-2.5 w-2.5 rounded-full" style={{ backgroundColor: CORES_TIPO_ATENDIMENTO[tipo].backgroundColor }} />
            <span>{tipoAtendimentoLabels[tipo]} : {total}</span>
          </div>
        ))}
      </div>
    </div>
  );
}
