import { Button } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { useGetEmployeeById } from "@/kubb";
import { BrushCleaning, FileUser } from "lucide-react";
import { useState } from "react";
import { useParams } from "react-router-dom";
import { EmployeeKPIs } from "../components/EmployeeKPIs";
import { EmployeeEventsTable } from "../components/EmployeeEventsTable";
import { DateRangeInput } from "@/components/ui/date-range-input";
import { useEmployeeDateFilters } from "../hooks/use-employee-date-filters";
import { EmployeeInfoSection } from "../components/EmployeeInfoSection";
import { EmployeeEditModal } from "../components/EmployeeEditModal";

const headerProps = {
  description: "Veja e gerencie as informações do colaborador",
  title: "Detalhes do colaborador",
  Icon: FileUser,
  backLink: "/employees",
};

export function EmployeeDetailPage() {
  const { id } = useParams<{ id: string }>();
  const employeeId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);

  const {
    startDate,
    endDate,
    handleStartDateChange,
    handleEndDateChange,
    handleClearFilters,
    hasFilters,
  } = useEmployeeDateFilters();

  const employeeQuery = useGetEmployeeById(employeeId);

  return (
    <PageLayout {...headerProps}>
      <div className="mb-3">
        <EmployeeInfoSection employeeId={employeeId} onEdit={() => setIsFormOpen(true)} />
      </div>

        <div className="flex justify-between gap-2 items-center bg-base-100 p-4 rounded-xl border border-base-300 shadow-sm mb-3">
          <h3 className="text-lg font-bold text-base-content/80">
              Indicadores e Filtros
            </h3>
          <DateRangeInput
            startDate={startDate}
            endDate={endDate}
            onStartDateChange={handleStartDateChange}
            onEndDateChange={handleEndDateChange}
          />
          {hasFilters && (
            <div className="tooltip" data-tip="Limpar datas">
              <Button size="sm" variant="outline" onClick={handleClearFilters}>
                <BrushCleaning size={16} />
              </Button>
            </div>
          )}
        </div>

      <div className="mb-3 animate-[fade-up_600ms_ease-out_both]">
        <EmployeeKPIs employeeId={employeeId} />
      </div>

      <div className="animate-[fade-up_600ms_ease-out_both]">
        <EmployeeEventsTable employeeId={employeeId} />
      </div>

      {isFormOpen && employeeQuery.data && (
        <EmployeeEditModal employee={employeeQuery.data} isOpen={isFormOpen} onClose={() => setIsFormOpen(false)} />
      )}
    </PageLayout>
  );
}
