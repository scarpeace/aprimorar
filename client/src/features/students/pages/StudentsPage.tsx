import { Button } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { type AlunoResponseDTO } from "@/kubb";
import { Check, GraduationCap, Plus } from "lucide-react";
import { useState } from "react";
import { StudentsTable } from "../components/StudentsTable";
import { StudentForm } from "../components/StudentForm";
import { ParentsTable } from "@/features/parents/components/ParentsTable";
import { StudentsAndParentsKpis } from "../components/StudentsAndParentsKpis";

export function StudentsPage() {
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedStudent, setSelectedStudent] = useState<AlunoResponseDTO | null>(null);

  const headerProps = {
    description: "Resumo de alunos e responsáveis.",
    title: "Alunos e Responsáveis",
    Icon: GraduationCap,
    backLink: "/",
  };

  const handleOpenForm = (student?: AlunoResponseDTO) => {
    setSelectedStudent(student || null);
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setSelectedStudent(null);
    setIsFormOpen(false);
  };

  return (
    <PageLayout {...headerProps}>

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
                <Button onClick={() => handleOpenForm()} variant="success"><Plus size={16} />Novo Responsável</Button>
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
    </PageLayout>
  );
}
