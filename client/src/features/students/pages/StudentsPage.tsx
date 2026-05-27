import { Button } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { type AlunoResponseDTO } from "@/kubb";
import { GraduationCap, Plus } from "lucide-react";
import { useState } from "react";
import { StudentsTable } from "../components/StudentsTable";
import { StudentForm } from "../components/StudentForm";
import { ParentsTable } from "@/features/parents/components/ParentsTable";

export function StudentsPage() {
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedStudent, setSelectedStudent] = useState<AlunoResponseDTO | null>(null);

  const headerProps = {
    description: "Gerencie cadastros e matrículas.",
    title: "Alunos",
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
      <main className="flex flex-row gap-6 rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
            <section className="w-1/2 border rounded-2xl border-base-300 p-3">
              <div className="flex justify-between items-center">
                <div>
                  <h3 className="text-2xl font-bold text-base-content">Alunos</h3>
                  <p className="text-sm text-base-content/60">Clique na linha para abrir os detalhes do cadastro.</p>
                </div>
                <Button onClick={() => handleOpenForm()} variant="success"><Plus size={16} />Novo Aluno</Button>
              </div>

              <StudentsTable />
            </section>

            <section className="w-1/2 border rounded-2xl border-base-300 p-3">
              <div className="flex justify-between items-center">
                <div>
                  <h3 className="text-2xl font-bold text-base-content">Responsáveis</h3>
                  <p className="text-sm text-base-content/60">Clique na linha para abrir os detalhes do cadastro.</p>
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
