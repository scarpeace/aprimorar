import { KpiCard } from "@/components/Ui/KpiCard.tsx";
import { PageLayout } from "@/components/Layout/PageLayout.tsx";
import { ResponsaveisTable } from "@/components/Responsavel/ResponsaveisTable.tsx";
import { useGetAlunosKpis } from "@/kubb";
import { GraduationCap, UserCheck, UserCircle } from "lucide-react";
import { AlunosTable } from "../../components/Aluno/AlunosTable.tsx";
import { Modal } from "@/components/Ui/Modal.tsx";
import { Suspense, useState } from "react";
import { AlunoForm } from "../../components/Aluno/AlunoForm.tsx";
import { ResponsavelForm } from "@/components/Responsavel/ResponsavelForm.tsx";

const HEADER_PROPS = {
  description: "Resumo de alunos e responsáveis.",
  title: "Aluno e Responsáveis",
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
              description="Informe os dados do aluno para efetuar a matrícula, um aluno deve ter um responsável no momento do seu cadastro."
              size="lg"
            >
              <Suspense fallback={<p className="text-sm text-base-content/60">Carregando formulário...</p>}>
                <AlunoForm
                  onSuccess={() => setIsAlunoFormOpen(false)}
                  onCancel={() => setIsAlunoFormOpen(false)}
                />
              </Suspense>
        </Modal>

        <Modal
          isOpen={isResponsavelFormOpen}
          onClose={() => setIsResponsavelFormOpen(false)}
          title="Cadastrar Novo Responsável"
          description="Informe os dados do responsável para efetuar o cadastro."
          size="lg"
        >
          <Suspense fallback={<p className="text-sm text-base-content/60">Carregando formulário...</p>}>
            <ResponsavelForm
              onSuccess={() => setIsResponsavelFormOpen(false)}
              onCancel={() => setIsResponsavelFormOpen(false)}
            />
          </Suspense>
        </Modal>
      </PageLayout>
  );
}
