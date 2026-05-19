import { Button } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { PageLayout } from "@/components/layout/PageLayout";
import { useGetStudentSummary, useGetStudentsWithFinance } from "@/kubb";
import type { StudentResponseDTO } from "@/kubb";
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
  const [selectedStudent, setSelectedStudent] = useState<StudentResponseDTO | null>(null);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const studentsWithFinanceQuery = useGetStudentsWithFinance({
    page: currentPage,
    search: debouncedSearchTerm,
    archived: showArchived,
  });

  const studentSummaryQuery = useGetStudentSummary();

  const displayedStudents = useMemo(() => {
    if (!studentsWithFinanceQuery.data || !hideCharged) {
      return studentsWithFinanceQuery.data;
    }

    return {
      ...studentsWithFinanceQuery.data,
      content: (studentsWithFinanceQuery.data.content ?? []).filter(
        (student) => (student.totalPending ?? 0) > 0,
      ),
    };
  }, [hideCharged, studentsWithFinanceQuery.data]);

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

  const handleOpenForm = (student?: StudentResponseDTO) => {
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
      <div className="flex w-full flex-col gap-4">
        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_180ms_ease-out_both]">
          <div className="flex flex-row items-center justify-between">
            <div>
              <h3 className="text-2xl font-bold text-base-content">Indicadores de alunos</h3>
              <p className="text-sm text-base-content/60">
                Visão geral dos alunos ativos e do total cadastrado desde o inicio.
              </p>
            </div>

            <div className="flex gap-3">
            <KpiCard
              label="Alunos ativos"
              value={studentSummaryQuery.data?.activeStudents ?? 0}
              Icon={UserCheck}
              className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
            />

            <KpiCard
              label="Desde o início"
              value={studentSummaryQuery.data?.totalStudents ?? 0}
                Icon={Users}
              className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
              />
            </div>

          </div>
        </section>

        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_220ms_ease-out_both]">
          <div className="flex flex-col gap-3 lg:flex-row lg:items-center lg:justify-between">
            <div>
              <h2 className="text-2xl font-bold text-base-content">Busca e filtros</h2>
              <p className="text-sm text-base-content/60">
                Localize alunos por cadastro.
              </p>
              </div>


            <ListSearchInput
              className="grow"
              placeholder="Buscar aluno por nome, email ou CPF"
              ariaLabel="Buscar aluno"
              value={searchTerm}
              onChange={setSearchTerm}
            />
            <div className="flex w-full flex-col items-start justify-between gap-3 xl:w-auto xl:justify-end">
              <ToggleSwitch
                label="Arquivados"
                tip="Mostrar alunos arquivados"
                toggled={showArchived}
                setToggle={handleShowArchivedChange}
                className="border-info/25 bg-base-100 shadow-sm checked:border-info checked:bg-info checked:text-info-content"
              />
              <ToggleSwitch
                label="Ocultar cobrados"
                tip="Mostrar apenas alunos com pendencias no periodo"
                toggled={hideCharged}
                setToggle={handleHideChargedChange}
                className="border-warning/25 bg-base-100 shadow-sm checked:border-warning checked:bg-warning checked:text-warning-content"
              />
            </div>
            </div>
        </section>

        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
          <div className="mb-4 flex flex-col gap-1 sm:flex-row sm:items-center sm:justify-between">
              <div>
              <h3 className="text-2xl font-bold text-base-content">Alunos cadastrados</h3>
              <p className="text-sm text-base-content/60">
                Clique na linha para abrir os detalhes do cadastro.
                </p>
                </div>

              <Button
                className="sm:ml-auto"
                onClick={() => handleOpenForm()}
                variant="success"
              >
                <Plus className="mr-2 h-4 w-4" />
                Novo Aluno
              </Button>
          </div>

          <StudentsTable
            students={displayedStudents}
            onPageChange={setCurrentPage}
            currentPage={currentPage}
            isPending={studentsWithFinanceQuery.isPending}
            error={studentsWithFinanceQuery.error}
          />
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
