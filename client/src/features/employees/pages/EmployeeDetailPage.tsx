import { PageLayout } from "@/components/layout/PageLayout";
import {
  useGetAppointmentsByEmployeeId,
  useGetEmployeeById,
} from "@/kubb";
import { CircleDollarSign, FileUser } from "lucide-react";
import { useState } from "react";
import { useParams } from "react-router-dom";
import { EmployeeKPIs } from "../components/EmployeeKPIs";
import { EmployeeEventsTable } from "../components/EmployeeEventsTable";
import { EmployeeInfoSection } from "../components/EmployeeInfoSection";
import { EmployeeEditModal } from "../components/EmployeeEditModal";
import { useDateFilter } from "@/hooks/use-date-filter";
import { ToggleSwitch } from "@/components/ui/toggle-switch";

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
  const [currentPage, setCurrentPage] = useState(0);
  const [hidePaid, setHidePaid] = useState(false);

  const { startDate, endDate } = useDateFilter();

  const employeeQuery = useGetEmployeeById(employeeId);

  const employeeAppointments = useGetAppointmentsByEmployeeId(employeeId, {
    page: currentPage,
    sort: ["endDate,desc", "id,asc"],
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
    hidePaid,
  });

  const handleToggleHidePaid = () => {
    setHidePaid((current) => !current);
    setCurrentPage(0);
  };

  return (
    <PageLayout {...headerProps}>
      <div className="mb-3">
        <EmployeeInfoSection employeeId={employeeId} onEdit={() => setIsFormOpen(true)} />
      </div>

        <div className="flex justify-between gap-2 items-center bg-base-100 p-4 rounded-xl border border-base-300 shadow-sm mb-3">
          <h3 className="text-lg font-bold text-base-content/80">
              Indicadores e Filtros
            </h3>
          <div className="shrink-0 rounded-2xl border border-info/20 bg-linear-to-r from-info/8 via-base-100 to-base-100 px-3 py-2 shadow-sm transition-all duration-200 hover:border-info/30 hover:shadow-md">
            <div className="flex items-center gap-3">
              <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-info/12 text-info">
                <CircleDollarSign className="w-4" />
              </div>
              <ToggleSwitch
                toggled={hidePaid}
                setToggle={handleToggleHidePaid}
                label={"Ocultar Pagos"}
                className="border-info/30 bg-base-100 shadow-sm checked:border-info checked:bg-info checked:text-info-content"
              />
            </div>
          </div>
        </div>

      <div className="mb-3 animate-[fade-up_600ms_ease-out_both]">
        <EmployeeKPIs
          totalEvents={employeeAppointments.data?.summary?.totalEvents}
          totalPaid={employeeAppointments.data?.summary?.totalPaid}
          totalUnpaid={employeeAppointments.data?.summary?.totalUnpaid}
        />
      </div>

      <div className="animate-[fade-up_600ms_ease-out_both]">
        <EmployeeEventsTable
          appointments={employeeAppointments.data?.appointments}
          error={employeeAppointments.error}
          isLoading={employeeAppointments.isLoading}
          currentPage={currentPage}
          onPageChange={setCurrentPage}
        />
      </div>

      {isFormOpen && employeeQuery.data && (
        <EmployeeEditModal employee={employeeQuery.data} isOpen={isFormOpen} onClose={() => setIsFormOpen(false)} />
      )}
    </PageLayout>
  );
}
