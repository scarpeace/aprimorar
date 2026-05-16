import { Button } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { PageLayout } from "@/components/layout/PageLayout";
import { useGetStudents, useGetStudentsAppointmentsFinanceReport } from "@/kubb";
import type { StudentResponseDTO } from "@/kubb";
import { useDateFilter } from "@/hooks/use-date-filter";
import { useDebounce } from "@/lib/shared/use-debounce";
import { GraduationCap, Plus } from "lucide-react";
import { useMemo, useState } from "react";
import { StudentsTable } from "../components/StudentsTable";
import { StudentForm } from "../components/StudentForm";
import { StudentKPIs } from "../components/StudentKPIs";

export function StudentsPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);
  const [hideCharged, setHideCharged] = useState(false);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedStudent, setSelectedStudent] = useState<StudentResponseDTO | null>(null);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  const { startDate, endDate } = useDateFilter();

  const studentsQuery = useGetStudents({
    page: currentPage,
    search: debouncedSearchTerm,
    archived: showArchived,
  });
  const studentsFinanceQuery = useGetStudentsAppointmentsFinanceReport({
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
  });

  const financeRows = useMemo(
    () => studentsFinanceQuery.data?.students ?? [],
    [studentsFinanceQuery.data?.students],
  );

  const chargedByStudentId = useMemo(
    () =>
      new Map<string, number>(
        financeRows.flatMap((student) =>
          student.studentId
            ? [[student.studentId, student.totalCharged ?? 0] as const]
            : [],
        ),
      ),
    [financeRows],
  );

  const pendingByStudentId = useMemo(
    () =>
      new Map<string, number>(
        financeRows.flatMap((student) =>
          student.studentId
            ? [[student.studentId, student.totalPending ?? 0] as const]
            : [],
        ),
      ),
    [financeRows],
  );

  const studentFinanceSummary = useMemo(() => {
    return financeRows.reduce(
      (summary, student) => ({
        totalEvents: summary.totalEvents + (student.totalEvents ?? 0),
        totalCharged: summary.totalCharged + (student.totalCharged ?? 0),
        totalPending: summary.totalPending + (student.totalPending ?? 0),
      }),
      { totalEvents: 0, totalCharged: 0, totalPending: 0 } as {
        totalEvents: number;
        totalCharged: number;
        totalPending: number;
      },
    );
  }, [financeRows]);

  const displayedStudents = useMemo(() => {
    if (!studentsQuery.data || !hideCharged) {
      return studentsQuery.data;
    }

    return {
      ...studentsQuery.data,
      content: studentsQuery.data.content.filter(
        (student) => (pendingByStudentId.get(student.id) ?? 0) > 0,
      ),
    };
  }, [hideCharged, pendingByStudentId, studentsQuery.data]);

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

  return (
    <PageLayout {...headerProps}>
      <div className="flex w-full flex-col gap-4">
        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_220ms_ease-out_both]">
          <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
            <div className="space-y-1">
              <h2 className="text-lg font-bold text-base-content">Busca e filtros</h2>
              <p className="text-sm text-base-content/60">
                Localize alunos por cadastro e alterne entre registros ativos e arquivados sem sair da listagem.
              </p>
            </div>

            <div className="rounded-2xl border border-success/15 bg-linear-to-r from-success/8 via-base-100 to-base-100 px-3 py-2 shadow-sm">
              <Button
                className="sm:ml-auto"
                onClick={() => handleOpenForm()}
                variant="success"
              >
                <Plus className="mr-2 h-4 w-4" />
                Novo Aluno
              </Button>
            </div>
          </div>

          <div className="mt-4 flex flex-col gap-3 xl:flex-row xl:items-center">
            <ListSearchInput
              className="grow"
              placeholder="Buscar aluno por nome, email ou CPF"
              ariaLabel="Buscar aluno"
              value={searchTerm}
              onChange={setSearchTerm}
            />
            <div className="flex w-full flex-wrap items-center justify-between gap-3 xl:w-auto xl:justify-end">
              <ToggleSwitch
                label="Arquivados"
                tip="Mostrar alunos arquivados"
                toggled={showArchived}
                setToggle={setShowArchived}
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

        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_260ms_ease-out_both]">
          <div className="mb-4">
            <h3 className="text-lg font-bold text-base-content">Resumo financeiro dos alunos</h3>
            <p className="text-sm text-base-content/60">
              Indicadores consolidados respeitando o periodo selecionado nos filtros.
            </p>
          </div>

          <StudentKPIs
            totalEvents={studentFinanceSummary.totalEvents}
            totalCharged={studentFinanceSummary.totalCharged}
            totalPending={studentFinanceSummary.totalPending}
          />
        </section>

        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
          <div className="mb-4 flex flex-col gap-1 sm:flex-row sm:items-end sm:justify-between">
            <div>
              <h3 className="text-lg font-bold text-base-content">Alunos cadastrados</h3>
              <p className="text-sm text-base-content/60">
                Clique na linha para abrir os detalhes do cadastro.
              </p>
            </div>
          </div>

          <StudentsTable
            students={displayedStudents}
            chargedByStudentId={chargedByStudentId}
            pendingByStudentId={pendingByStudentId}
            onPageChange={setCurrentPage}
            currentPage={currentPage}
            isPending={studentsQuery.isPending || studentsFinanceQuery.isPending}
            error={studentsQuery.error ?? studentsFinanceQuery.error}
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
