import { ButtonLink } from "@/components/ui/button";
import { SectionCard } from "@/components/ui/section-card";
import { EventTable } from "@/features/events/components/EventTable";
import type { GetEventsByStudent200 } from "@/kubb";

type StudentEventsSectionProps = {
  studentId: string;
  events: GetEventsByStudent200;
  isLoading: boolean;
  error: unknown;
  currentPage: number;
  onPageChange: (page: number) => void;
};

export function StudentEventsTable({
  events,
  isLoading,
  error,
  currentPage,
  onPageChange,
}: Readonly<StudentEventsSectionProps>) {
  return (
    <SectionCard
      title="Eventos vinculados"
      description="Todos os eventos vinculados a este aluno."
    >
      <EventTable
        variant="page"
        context="student"
        data={events}
        isLoading={isLoading}
        error={error}
        currentPage={currentPage}
        onPageChange={onPageChange}
        itemName="eventos"
        renderActions={(event) => (
          <ButtonLink to={`/events/${event.eventId}`} size="sm" variant="outline">
            Detalhes
          </ButtonLink>
        )}
      />
    </SectionCard>
  );
}
