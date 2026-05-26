import { Button } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { PageLayout } from "@/components/layout/PageLayout";
// import { useGetStudentSummary, useGetStudentsWithFinance } from "@/kubb";
import { useGetAlunos, type AlunoResponseDTO } from "@/kubb";
import { useDebounce } from "@/lib/hooks/use-debounce.ts";
import { GraduationCap, Plus, UserCheck, Users } from "lucide-react";
import { useMemo, useState } from "react";
import { StudentsTable } from "../components/StudentsTable";
import { StudentForm } from "../components/StudentForm";
import { KpiCard } from "@/components/ui/kpi-card";

export function StudentsPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);
  const [hideCharged, setHideCharged] = useState(false);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedStudent, setSelectedStudent] = useState<AlunoResponseDTO | null>(null);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const studentsQuery = useGetAlunos({
    page: currentPage,
    search: debouncedSearchTerm,
    archived: showArchived,
  });

  // const studentSummaryQuery = useGetStudentSummary();

  // const displayedStudents = useMemo(() => {
  //   if (!studentsWithFinanceQuery.data || !hideCharged) {
  //     return studentsWithFinanceQuery.data;
  //   }

  //   return {
  //     ...studentsWithFinanceQuery.data,
  //     content: (studentsWithFinanceQuery.data.content ?? []).filter(
  //       (student) => (student.totalPending ?? 0) > 0,
  //     ),
  //   };
  // }, [hideCharged, studentsWithFinanceQuery.data]);

  const handleHideChargedChange = (value: boolean) => {
    setHideCharged(value);
    setCurrentPage(0);
  };

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

  const handleShowArchivedChange = (value: boolean) => {
    setShowArchived(value);
    setCurrentPage(0);
  };

  return (
    <PageLayout {...headerProps}>
      <div className="flex w-full flex-col gap-3">
        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_180ms_ease-out_both]">
          <div className="flex flex-col gap-3 justify-between">
            <div>
              <h3 className="text-2xl font-bold text-base-content">Indicadores de alunos</h3>
              <p className="text-sm text-base-content/60">
                Visão geral dos alunos ativos e do total cadastrado desde o inicio.
              </p>
            </div>

            <div className="flex justify-between">
            <KpiCard
              label="Alunos ativos (Mockado)"
              value={29}
              Icon={UserCheck}
              className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
            />

            <KpiCard
              label="Desde o início (Mockado)"
              value={200}
              Icon={Users}
              className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
              />

              <KpiCard
                label="Pago desde o início (Mockado)"
                value={20000}
                Icon={Users}
                className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
                />
            </div>

          </div>
        </section>

        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
          <div className="mb-4 flex flex-col gap-1 sm:flex-row sm:items-center sm:justify-between">
              <div>
                <h3 className="text-2xl font-bold text-base-content">Alunos cadastrados</h3>
                <p className="text-sm text-base-content/60">Clique na linha para abrir os detalhes do cadastro.</p>
              </div>

              <Button className="sm:ml-auto" onClick={() => handleOpenForm()} variant="success">
                <Plus className="mr-2 h-4 w-4" />
                Novo Aluno
              </Button>
          </div>

          <StudentsTable/>
        </section>

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
      </div>
    </PageLayout>
  );
}
