import { PageLayout } from "@/components/layout/PageLayout";
import {
  useGetAppointmentsByEmployeeId,
  useGetEmployeeById,
} from "@/kubb";
import { FileUser } from "lucide-react";
import { useState } from "react";
import { useParams } from "react-router-dom";
import { EmployeeKPIs } from "../components/EmployeeKPIs";
import { EmployeeEventsTable } from "../components/EmployeeEventsTable";
import { EmployeeInfoSection } from "../components/EmployeeInfoSection";
import { EmployeeEditModal } from "../components/EmployeeEditModal";
import { useDateFilter } from "@/hooks/use-date-filter";

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

  const handleToggleHidePaid = (value: boolean) => {
    setHidePaid(value);
    setCurrentPage(0);
  };

  return (
    <PageLayout {...headerProps}>
      <div className="mb-3">
        <EmployeeInfoSection employeeId={employeeId} onEdit={() => setIsFormOpen(true)} />
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
          hidePaid={hidePaid}
          isLoading={employeeAppointments.isLoading}
          currentPage={currentPage}
          onHidePaidChange={handleToggleHidePaid}
          onPageChange={setCurrentPage}
        />
      </div>

      {isFormOpen && employeeQuery.data && (
        <EmployeeEditModal employee={employeeQuery.data} isOpen={isFormOpen} onClose={() => setIsFormOpen(false)} />
      )}
    </PageLayout>
  );
}
