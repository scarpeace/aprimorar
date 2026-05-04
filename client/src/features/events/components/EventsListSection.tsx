import { useGetEvents } from "@/kubb";
import { EventsTable } from "./EventsTable";
import { useDebounce } from "@/lib/shared/use-debounce";

type EventsListSectionProps = {
  search: string;
  startDate: Date | null;
  endDate: Date | null;
  hideCharged: boolean;
  hidePaid: boolean;
  page: number;
  onPageChange: (page: number) => void;
};

export function EventsListSection({
  search,
  startDate,
  endDate,
  hideCharged,
  hidePaid,
  page,
  onPageChange,
}: EventsListSectionProps) {
  const debouncedSearch = useDebounce(search, 500);

  const eventsQuery = useGetEvents({
    page: page,
    size: 20,
    search: debouncedSearch,
    sort: ["startDate,desc", "id,asc"],
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
    hideCharged,
    hidePaid,
  });

  return (
    <EventsTable
      eventsPage={eventsQuery.data}
      currentPage={page}
      onPageChange={onPageChange}
      isPending={eventsQuery.isPending}
      error={eventsQuery.error}
    />
  );
}
