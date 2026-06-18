import { Button } from "@/components/ui/button";
import { KpiCard } from "@/components/ui/kpi-card";
import { PageLayout } from "@/components/layout/PageLayout";
import { ResponsaveisTable } from "@/features/responsaveis/components/ResponsaveisTable";
import { useGetAlunosKpis } from "@/kubb";
import { GraduationCap, Plus, UserCheck, UserCircle } from "lucide-react";
import { AlunosTable } from "../components/AlunosTable";

const HEADER_PROPS = {
  description: "Resumo de alunos e responsáveis.",
  title: "Alunos e Responsáveis",
  Icon: GraduationCap,
  iconBg: "success",
} as const;

export function AlunosPage() {
  const { data: kpisAlunos } = useGetAlunosKpis();

  return (
    <PageLayout {...HEADER_PROPS}>

      <section className="mb-3 rounded-2xl bg-base-100 p-4 shadow-sm animate-[fade-up_180ms_ease-out_both]">
        <div className="flex flex-row justify-between items-center gap-3">
          <div>
            <h3 className="text-2xl font-bold text-base-content">Resumo dos Alunos</h3>
            <p className="text-sm text-base-content/60">
              Visão geral dos alunos ativos e do total cadastrado desde o início.
            </p>
          </div>

          <div className="flex gap-3">
            <KpiCard
              label="Ativos atualmente"
              value={kpisAlunos?.totalAlunosAtivos}
              Icon={UserCheck}
              className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
            />
            <KpiCard
              label="Total Desde o início"
              value={kpisAlunos?.totalAlunos}
              Icon={UserCircle}
              className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
            />
          </div>
        </div>
      </section>

      <main className="grid grid-cols-1 gap-6 rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both] 2xl:grid-cols-2">
            <section className="min-w-0 rounded-2xl border border-base-300 p-3">
              <div className="flex justify-between items-center">
                <div>
                  <h3 className="text-2xl font-bold text-base-content">Alunos</h3>
                  <p className="text-sm text-base-content/60">Selecione um aluno para ver os detalhes.</p>
                </div>
                <Button onClick={() => null} variant="success"><Plus size={16} />Novo Aluno</Button>
              </div>

              <AlunosTable />
            </section>

            <section className="min-w-0 rounded-2xl border border-base-300 p-3">
              <div className="flex justify-between items-center">
                <div>
                  <h3 className="text-2xl font-bold text-base-content">Responsáveis</h3>
                  <p className="text-sm text-base-content/60">Selecione um responsável para ver os detalhes.</p>
                </div>
                <Button onClick={() => null} variant="success"><Plus size={16} />Novo Responsável</Button>
              </div>

              <ResponsaveisTable />
            </section>


        </main>
      </PageLayout>
  );
}
