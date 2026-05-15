import { PageLayout } from "@/components/layout/PageLayout";
import {
  useGetAppointmentsByStudentId,
  useGetStudentById,
} from "@/kubb";
import {
  Calendar,
  CircleDollarSign,
  Clock3,
  GraduationCap,
} from "lucide-react";
import { useState } from "react";
import { useParams } from "react-router-dom";
import { StudentEventsTable } from "../components/StudentEventsTable";
import { DateRangeInput } from "@/components/ui/date-range-input";
import { StudentInfoSection } from "../components/StudentInfoSection";
import { StudentForm } from "../components/StudentForm";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { KpiCard } from "@/components/ui/kpi-card";
import { brl } from "@/lib/utils/formatter";
import { useDateRangeFilters } from "@/hooks/use-date-range-filters";

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
    handleStartDateChange,
    handleEndDateChange,
  } = useDateRangeFilters();

  const studentAppointments = useGetAppointmentsByStudentId(studentId, {
    page: currentPage,
    sort: ["endDate,desc", "id,asc"],
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
    charged: hideCharged ? false : undefined,
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
    setCurrentPage(0);
    handleEndDateChange(date);
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
        <div className="shrink-0 rounded-2xl border border-warning/20 bg-linear-to-r from-warning/8 via-base-100 to-base-100 px-3 py-2 shadow-sm transition-all duration-200 hover:border-warning/30 hover:shadow-md">
          <div className="flex items-center gap-3">
            <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-warning/12 text-warning">
              <CircleDollarSign className="w-4" />
            </div>
              <ToggleSwitch
                toggled={hideCharged}
                setToggle={handleToggleHideCharged}
                label={"Ocultar Cobrados"}
                className="border-warning/30 bg-base-100 shadow-sm checked:border-warning checked:bg-warning checked:text-warning-content"
              />
          </div>
        </div>
        <div className="w-full sm:w-auto">
          <DateRangeInput
            startDate={startDate ?? null}
            endDate={endDate ?? null}
            onStartDateChange={handleStartDateFilterChange}
            onEndDateChange={handleEndDateFilterChange}
          />
        </div>
      </div>

      <div className="mb-3 animate-[fade-up_600ms_ease-out_both]">
        <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
          <KpiCard
            label="Total de eventos"
            value={studentAppointments.data?.summary?.totalEvents}
            Icon={Calendar}
          />

          <KpiCard
            label="Total pago"
            value={<span className="text-success">{brl.format(studentAppointments.data?.summary?.totalCharged ?? 0)}</span>}
            Icon={CircleDollarSign}
            className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
          />

          <KpiCard
            label="Total pendente"
            value={<span className="text-warning">{brl.format(studentAppointments.data?.summary?.totalPending ?? 0)}</span>}
            Icon={Clock3}
            className="bg-linear-to-br from-warning/10 via-base-100 to-base-100"
          />
        </div>
      </div>

      <div className="animate-[fade-up_600ms_ease-out_both]">
        <StudentEventsTable
          appointments={studentAppointments.data?.appointments}
          error={studentAppointments.error}
          isLoading={studentAppointments.isLoading}
          currentPage={currentPage}
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
