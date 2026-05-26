import { useGetAtendimentos } from "@/kubb";
import { AppointmentsTable } from "./AppointmentsTable";

type AppointmentsListSectionProps = {
  page: number;
  search: string;
  hideCharged: boolean;
  hidePaid: boolean;
  onPageChange: (page: number) => void;
};

export function AppointmentsListSection({
  page,
  search,
  hideCharged,
  hidePaid,
  onPageChange,
}: AppointmentsListSectionProps) {
  const eventsQuery = useGetAtendimentos({
    page,
    size: 20,
    sort: ["startDate,desc", "id,asc"],
    search: search || undefined,
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
