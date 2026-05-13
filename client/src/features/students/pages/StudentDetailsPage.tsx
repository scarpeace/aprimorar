import { Button } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { useAppointmentMutations } from "@/features/appointments/hooks/use-appointment-mutations";
import {
  useGetAppointmentsByStudentId,
  useGetStudentById,
  useGetStudentSummary,
} from "@/kubb";
import { BrushCleaning, GraduationCap } from "lucide-react";
import { useState } from "react";
import { useParams } from "react-router-dom";
import { StudentKPIs } from "../components/StudentKPIs";
import { StudentEventsTable } from "../components/StudentEventsTable";
import { DateRangeInput } from "@/components/ui/date-range-input";
import { useStudentDateFilters } from "../hooks/use-student-date-filters";
import { StudentInfoSection } from "../components/StudentInfoSection";
import { StudentForm } from "../components/StudentForm";
import { ToggleSwitch } from "@/components/ui/toggle-switch";

const headerProps = {
  description: "Veja e gerencie as informações do aluno",
  title: "Detalhes do aluno",
  Icon: GraduationCap,
  backLink: "/students",
};

export function StudentDetailsPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [hideCharged, setHideCharged] = useState(false);

  const studentQuery = useGetStudentById(studentId);

  const {
    startDate,
    endDate,
    hasFilters,
    handleStartDateChange,
    handleEndDateChange,
    handleClearFilters,
  } = useStudentDateFilters();

  const { toggleStudentCharge } = useAppointmentMutations();

  const studentAppointments = useGetAppointmentsByStudentId(studentId, {
    page: currentPage,
    sort: ["endDate,desc", "id,asc"],
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
    charged: hideCharged ? false : undefined,
  });

  const studentSummary = useGetStudentSummary(studentId, {
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
  });

  const handleToggleHideCharged = () => {
    setHideCharged((current) => !current);
    setCurrentPage(0);
  };

  const handleStartDateFilterChange = (date: Date | null) => {
    setCurrentPage(0);
    handleStartDateChange(date);
  };

  const handleEndDateFilterChange = (date: Date | null) => {
    console.log(date);
    setCurrentPage(0);
    handleEndDateChange(date);
  };

  const handleClearDateFilters = () => {
    setCurrentPage(0);
    handleClearFilters();
  };



  return (
    <PageLayout {...headerProps}>
      <div className="mb-3">
        <StudentInfoSection
          studentId={studentId}
          onEdit={() => setIsFormOpen(true)}
        />
      </div>

      {/* STUDENT EVENTS FILTERS */}
      <div className="flex justify-between gap-2 items-center bg-base-100 p-4 rounded-xl border border-base-300 shadow-sm mb-3">
        <h3 className="text-lg font-bold text-base-content/80">
          Indicadores e Filtros
        </h3>
        <ToggleSwitch
          toggled={hideCharged}
          setToggle={handleToggleHideCharged}
          label={"Ocultar Cobrados"}
        />
        <div className="flex gap-2 items-center w-full sm:w-auto">
          <DateRangeInput
            startDate={startDate ?? null}
            endDate={endDate ?? null}
            onStartDateChange={handleStartDateFilterChange}
            onEndDateChange={handleEndDateFilterChange}
          />
          {hasFilters && (
            <div className="tooltip" data-tip="Limpar datas">
              <Button
                size="sm"
                variant="outline"
                onClick={handleClearDateFilters}
                className="h-10"
              >
                <BrushCleaning size={16} />
              </Button>
            </div>
          )}
        </div>
      </div>

      <div className="mb-3 animate-[fade-up_600ms_ease-out_both]">
        <StudentKPIs
          totalEvents={studentSummary.data?.totalEvents}
          totalCharged={studentSummary.data?.totalCharged}
          totalPending={studentSummary.data?.totalPending}
        />
      </div>

      <div className="animate-[fade-up_600ms_ease-out_both]">
        <StudentEventsTable
          appointments={studentAppointments.data}
          currentPage={currentPage}
          error={studentAppointments.error}
          isLoading={studentAppointments.isLoading}
          isTogglingCharge={toggleStudentCharge.isPending}
          onPageChange={setCurrentPage}
        />
      </div>

      {isFormOpen && (
        <div className="modal modal-open">
          <div className="modal-box max-w-4xl overflow-hidden">
            <h3 className="font-bold text-lg mb-4">Editar Aluno</h3>
            <StudentForm
              initialData={studentQuery.data}
              onSuccess={() => setIsFormOpen(false)}
              onCancel={() => setIsFormOpen(false)}
            />
          </div>
        </div>
      )}
    </PageLayout>
  );
}
