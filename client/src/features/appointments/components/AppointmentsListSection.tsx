import { useGetAppointments } from "@/kubb";
import { AppointmentsTable } from "./AppointmentsTable";

type AppointmentsListSectionProps = {
  page: number;
  search: string;
  startDate: Date | null;
  endDate: Date | null;
  hideCharged: boolean;
  hidePaid: boolean;
  onPageChange: (page: number) => void;
};

export function AppointmentsListSection({
  page,
  search,
  startDate,
  endDate,
  hideCharged,
  hidePaid,
  onPageChange,
}: AppointmentsListSectionProps) {
  const eventsQuery = useGetAppointments({
    page,
    size: 20,
    sort: ["startDate,desc", "id,asc"],
    search: search || undefined,
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
    hideCharged: hideCharged || undefined,
    hidePaid: hidePaid || undefined,
  });

  return (
    <AppointmentsTable
      appointments={eventsQuery.data}
      currentPage={page}
      onPageChange={onPageChange}
      isLoading={eventsQuery.isPending}
      error={eventsQuery.error}
    />
  );
}
