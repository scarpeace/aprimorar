import { Button } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { useGetStudentById } from "@/kubb";
import { BrushCleaning, GraduationCap } from "lucide-react";
import { useState, Suspense } from "react";
import { useParams } from "react-router-dom";
import { StudentKPIs } from "../components/StudentKPIs";
import { StudentEventsTable } from "../components/StudentEventsTable";
import { DateRangeInput } from "@/components/ui/date-range-input";
import { useStudentDateFilters } from "../hooks/use-student-date-filters";
import { StudentInfoSection } from "../components/StudentInfoSection";
import { StudentForm } from "../components/StudentForm";

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

  const {
    startDate,
    endDate,
    handleStartDateChange,
    handleEndDateChange,
    handleClearFilters,
    hasFilters,
  } = useStudentDateFilters();

  const studentQuery = useGetStudentById(studentId);

  return (
    <PageLayout {...headerProps}>
      <div className="flex flex-col gap-6 max-w-7xl mx-auto">

        <StudentInfoSection studentId={studentId} onEdit={() => setIsFormOpen(true)}/>

        {/* Section 2: Filters and KPIs */}
        <div className="flex flex-col gap-4 animate-[fade-up_450ms_ease-out_both]">
          <div className="flex flex-col sm:flex-row justify-between gap-4 items-center bg-base-100 p-4 rounded-xl border border-base-300 shadow-sm">
            <h3 className="text-lg font-bold text-base-content/80">
              Indicadores e Filtros
            </h3>
            <div className="flex gap-2 items-center w-full sm:w-auto">
              <DateRangeInput
                startDate={startDate}
                endDate={endDate}
                onStartDateChange={handleStartDateChange}
                onEndDateChange={handleEndDateChange}
              />
              {hasFilters && (
                <div className="tooltip" data-tip="Limpar datas">
                  <Button size="sm" variant="outline" onClick={handleClearFilters} className="h-10">
                    <BrushCleaning size={16} />
                  </Button>
                </div>
              )}
            </div>
          </div>

          <StudentKPIs studentId={studentId} />
        </div>

        {/* Section 3: Events Table */}
        <div className="animate-[fade-up_600ms_ease-out_both]">
          <StudentEventsTable studentId={studentId} />
        </div>

        {isFormOpen && (
          <div className="modal modal-open">
            <div className="modal-box max-w-4xl">
              <h3 className="font-bold text-lg mb-4">Editar Aluno</h3>
              <StudentForm
                initialData={studentQuery.data}
                onSuccess={() => setIsFormOpen(false)}
                onCancel={() => setIsFormOpen(false)}
              />
            </div>
          </div>
        )}
      </div>
    </PageLayout>
  );
}
