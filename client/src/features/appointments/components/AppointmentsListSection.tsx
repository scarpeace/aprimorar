import { useGetAppointments } from "@/kubb";
import { AppointmentsTable } from "./AppointmentsTable";

type AppointmentsListSectionProps = {
  page: number;
  onPageChange: (page: number) => void;
};

export function AppointmentsListSection({
  page,
  onPageChange,
}: AppointmentsListSectionProps) {
  const eventsQuery = useGetAppointments({
    page: page,
    size: 20,
    sort: ["startDate,desc", "id,asc"],
  });

  return (
    <AppointmentsTable
      eventsPage={eventsQuery.data}
      currentPage={page}
      onPageChange={onPageChange}
      isPending={eventsQuery.isPending}
      error={eventsQuery.error}
    />
  );
}
