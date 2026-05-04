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
      <div className="grid gap-3 animate-[fade-up_300ms_ease-out_both]">
        <EmployeeInfoSection 
          employeeId={employeeId} 
          onEdit={() => setIsFormOpen(true)} 
        />

        <div className="flex justify-end gap-2 items-center mb-1">
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

        <EmployeeKPIs employeeId={employeeId} />

        <EmployeeEventsTable employeeId={employeeId} />
      </div>

      {isFormOpen && employeeQuery.data && (
        <EmployeeEditModal
          employee={employeeQuery.data}
          isOpen={isFormOpen}
          onClose={() => setIsFormOpen(false)}
        />
      )}
    </PageLayout>
  );
}
