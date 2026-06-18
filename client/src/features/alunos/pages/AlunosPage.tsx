import { Button } from "@/components/ui/button";
import { KpiCard } from "@/components/ui/kpi-card";
import { PageLayout } from "@/components/layout/PageLayout";
import { ResponsaveisTable } from "@/features/responsaveis/components/ResponsaveisTable";
import { useGetAlunosKpis } from "@/kubb";
import { GraduationCap, UserCheck, UserCircle } from "lucide-react";
import { AlunosTable } from "../components/AlunosTable";
import { Modal } from "@/components/ui/modal";
import { Suspense, useState } from "react";
import { AlunoForm } from "../components/AlunoForm";
import { ResponsavelForm } from "@/features/responsaveis/components/ResponsavelForm";

const HEADER_PROPS = {
  description: "Resumo de alunos e responsáveis.",
  title: "Alunos e Responsáveis",
  Icon: GraduationCap,
  iconBg: "success",
} as const;

export function AlunosPage() {
  const { data: kpisAlunos } = useGetAlunosKpis();
  const [isAlunoFormOpen, setIsAlunoFormOpen] = useState(false);
  const [isResponsavelFormOpen, setIsResponsavelFormOpen] = useState(false);

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

      <section className="grid grid-cols-2 gap-6 rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both] 2xl:grid-cols-2">
            <div className="min-w-0 rounded-2xl border border-base-300 p-3">
              <AlunosTable openForm={() => setIsAlunoFormOpen(true)} />
            </div>

            <div className="min-w-0 rounded-2xl border border-base-300 p-3">
              <ResponsaveisTable openForm={() => setIsResponsavelFormOpen(true)} />
            </div>
      </section>

        <Modal
              isOpen={isAlunoFormOpen}
              onClose={() => setIsAlunoFormOpen(false)}
              title="Cadastrar Novo Aluno"
              description="Atualize dados pessoais, contato e vínculos do aluno para manter a secretaria organizada."
              size="lg"
            >
              <Suspense fallback={<p className="text-sm text-base-content/60">Carregando formulário...</p>}>
                <AlunoForm
                  initialData={null}
                  onSuccess={() => setIsAlunoFormOpen(false)}
                  onCancel={() => setIsAlunoFormOpen(false)}
                />
              </Suspense>
        </Modal>

        <Modal
          isOpen={isResponsavelFormOpen}
          onClose={() => setIsResponsavelFormOpen(false)}
          title="Cadastrar Novo Responsável"
          description="Atualize dados pessoais, contato e vínculos do responsável para manter a secretaria organizada."
          size="lg"
        >
          <Suspense fallback={<p className="text-sm text-base-content/60">Carregando formulário...</p>}>
            <ResponsavelForm
              initialData={null}
              onSuccess={() => setIsResponsavelFormOpen(false)}
              onCancel={() => setIsResponsavelFormOpen(false)}
            />
          </Suspense>
        </Modal>
      </PageLayout>
  );
}
