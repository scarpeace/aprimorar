import { KpiCard } from "@/components/ui/kpi-card";
import { useGetIndicadoresAtendimentos } from "@/kubb";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { EmptyCard } from "@/components/ui/empty-card";
import { Button } from "@/components/ui/button";
import { usePageDateFilter } from "@/lib/hooks/use-page-date-filter";
import { brl } from "@/lib/utils/formatter";
import { BrushCleaning, GraduationCap, BanknoteArrowUp, BanknoteArrowDown, UserCog, HandCoins, Plus, DollarSign, BadgeDollarSignIcon, BadgeDollarSign } from "lucide-react";
import { useMemo } from "react";
import { MiniCard } from "@/components/ui/mini-card";

type FinancialKpisProps = {
  totalDespesas: number;
  totalDespesasPendentes: number;
};


export function FinancialKpis({ totalDespesas, totalDespesasPendentes }: FinancialKpisProps) {
  const { startDate, endDate, clearDateFilters } = usePageDateFilter();

  const { data: indicadoresAtendimentosData, isError: indicadoresAtendimentosError, isLoading: indicadoresAtendimentosLoading } = useGetIndicadoresAtendimentos({
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
  });

  const totalPayout = useMemo(() => {
    const entradas = indicadoresAtendimentosData?.totalStudentCharged ?? 0;
    const saidasColaboradores = indicadoresAtendimentosData?.totalEmployeePaid ?? 0;
    const saidasDespesas = totalDespesas ?? 0;

    return entradas - (saidasColaboradores + saidasDespesas);
  }, [
    indicadoresAtendimentosData?.totalStudentCharged,
    indicadoresAtendimentosData?.totalEmployeePaid,
    totalDespesas,
  ]);

  if (indicadoresAtendimentosError) {
    return <ErrorCard title="Não foi possível carregar a listagem de Responsáveis" error={indicadoresAtendimentosError}/>;
  }

  if (indicadoresAtendimentosLoading) {
    return <LoadingSpinner text="Carregando Responsáveis..." />;
  }

  if (!indicadoresAtendimentosData || !indicadoresAtendimentosData) {
    return (
      <EmptyCard
        title="Nenhum responsável encontrado"
        description="Ajuste a busca ou o filtro de arquivados para localizar os cadastros desejados."
        action={<Button variant="outline" onClick={clearDateFilters}>Limpar filtros<BrushCleaning size={18} /></Button>}
      />
    );
  }

  return (
    <div>
      <section className="flex flex-col rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_180ms_ease-out_both]">
        <div className="flex justify-between items-start mb-4">
          <div className="flex flex-col">
            <h2 className="text-2xl font-bold text-base-content">Despesas gerais</h2>
            <p className="mt-1 max-w-3xl text-sm leading-6 text-base-content/60">
              Despesas operacionais registradas no periodo selecionado.
            </p>
          </div>
        </div>

        <div className="flex gap-4">
          <div className={`flex flex-col rounded-xl p-3 gap-4 border-2 border-base-300`}>
            <h1 className="text-2xl flex gap-3 items-center font-bold text-base-content">
              <GraduationCap size={30} />
              Alunos
            </h1>
            <MiniCard label={"Pago"} children={<span className="text-success">{brl.format(indicadoresAtendimentosData?.totalStudentCharged ?? 0)}</span>}/>
            <MiniCard label={"Pendente"} children={<span className="text-warning">{brl.format(indicadoresAtendimentosData?.totalStudentPending ?? 0)}</span>}/>
          </div>

          <div className={`flex flex-col rounded-xl p-3 gap-4 border-2 border-base-300`}>
            <h1 className="text-2xl flex gap-3 items-center font-bold text-base-content">
              <UserCog size={30} />
              Colaboradores
            </h1>
            <MiniCard label={"Pago"} children={<span className="text-success">{brl.format(indicadoresAtendimentosData?.totalEmployeePaid ?? 0)}</span>}/>
            <MiniCard label={"Pendente"} children={<span className="text-warning">{brl.format(indicadoresAtendimentosData?.totalEmployeePending ?? 0)}</span>}/>
          </div>

          <div className={`flex flex-col rounded-xl p-3 gap-4 border-2 border-base-300`}>
            <h1 className="text-xl flex gap-3 items-center font-bold text-base-content">
              <BadgeDollarSign size={30} />
              Despesas
            </h1>
            <MiniCard label={"Pagas"} children={<span className="text-success">{brl.format(totalDespesas)}</span>}/>
            <MiniCard label={"Pendentes"} children={<span className="text-warning">{brl.format(totalDespesasPendentes)}</span>}/>
          </div>

          <div className={`flex flex-col rounded-xl p-3 gap-4 border-2 border-base-300 ml-auto`}>
            <h1 className="text-xl flex gap-3 items-center font-bold text-base-content">
              <HandCoins size={30} />
              Lucro
            </h1>
            <KpiCard
              label="Lucro"
              value={<span className={totalPayout < 0 ? "text-error" : "text-success"}>{brl.format(totalPayout)}</span>}
              Icon={BanknoteArrowUp}
              className="bg-linear-to-br from-success/8 via-base-100 to-base-100 ml-auto"
            />
          </div>
        </div>
    </section>
    </div>
  );
}
