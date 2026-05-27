import { Button } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { ParentForm } from "@/features/parents/components/ParentForm";
import { ParentsTable } from "@/features/parents/components/ParentsTable";
import { useGetAlunosKpis, type AlunoResponseDTO, type ResponsavelResponseDTO } from "@/kubb";
import { GraduationCap, Plus, UserCheck, UserCircle } from "lucide-react";
import { useState } from "react";
import { StudentsTable } from "../components/StudentsTable";
import { StudentForm } from "../components/StudentForm";
import { KpiCard } from "@/components/ui/kpi-card";

export function StudentsPage() {
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [isParentFormOpen, setIsParentFormOpen] = useState(false);
  const [selectedStudent, setSelectedStudent] = useState<AlunoResponseDTO | null>(null);
  const [selectedParent, setSelectedParent] = useState<ResponsavelResponseDTO | null>(null);

  const headerProps = {
    description: "Resumo de alunos e responsáveis.",
    title: "Alunos e Responsáveis",
    Icon: GraduationCap,
    backLink: "/",
  };

  const { data: kpisAlunos } = useGetAlunosKpis();

  const handleOpenForm = (student?: AlunoResponseDTO) => {
    setSelectedStudent(student || null);
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setSelectedStudent(null);
    setIsFormOpen(false);
  };

  const handleOpenParentForm = (parent?: ResponsavelResponseDTO) => {
    setSelectedParent(parent || null);
    setIsParentFormOpen(true);
  };

  const handleCloseParentForm = () => {
    setSelectedParent(null);
    setIsParentFormOpen(false);
  };

  return (
    <PageLayout {...headerProps}>

      <section className="mb-3 rounded-2xl bg-base-100 p-4 shadow-sm animate-[fade-up_180ms_ease-out_both]">
        <div className="flex flex-row justify-between items-center gap-3">
          <div>
            <h3 className="text-2xl font-bold text-base-content">Resumo dos Colaboradores</h3>
            <p className="text-sm text-base-content/60">
              Visão geral dos colaboradores ativos e do total cadastrado desde o inicio.
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
                <Button onClick={() => handleOpenForm()} variant="success"><Plus size={16} />Novo Aluno</Button>
              </div>

              <StudentsTable />
            </section>

            <section className="min-w-0 rounded-2xl border border-base-300 p-3">
              <div className="flex justify-between items-center">
                <div>
                  <h3 className="text-2xl font-bold text-base-content">Responsáveis</h3>
                  <p className="text-sm text-base-content/60">Selecione um responsável para ver os detalhes.</p>
                </div>
                <Button onClick={() => handleOpenParentForm()} variant="success"><Plus size={16} />Novo Responsável</Button>
              </div>

              <ParentsTable />
            </section>


        </main>

        {isFormOpen && (
          <div className="modal modal-open">
            <div className="modal-box max-w-4xl border border-base-300 bg-base-100 shadow-2xl">
              <h3 className="mb-1 text-lg font-bold">
                {selectedStudent ? "Editar Aluno" : "Cadastrar Novo Aluno"}
              </h3>
              <p className="mb-4 text-sm text-base-content/60">
                Atualize dados pessoais, contato e vinculos do aluno para manter a secretaria organizada.
              </p>
              <StudentForm
                initialData={selectedStudent}
                onSuccess={handleCloseForm}
                onCancel={handleCloseForm}
              />
            </div>
          </div>
        )}

        {isParentFormOpen && (
          <div className="modal modal-open">
            <div className="modal-box max-w-2xl border border-base-300 bg-base-100 shadow-2xl">
              <h3 className="mb-1 text-lg font-bold">
                {selectedParent ? "Editar Responsável" : "Cadastrar Novo Responsável"}
              </h3>
              <p className="mb-4 text-sm text-base-content/60">
                Atualize os dados principais do responsável e mantenha os vínculos organizados.
              </p>
              <ParentForm
                initialData={selectedParent}
                onSuccess={handleCloseParentForm}
                onCancel={handleCloseParentForm}
              />
            </div>
          </div>
        )}
    </PageLayout>
  );
}
