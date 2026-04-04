import { PageHeader } from "@/components/ui/page-header";
import { EventsTable } from "@/features/events/components/EventsTable";
import { useEventsByStudent } from "@/features/events/hooks/use-event-queries";
import { useGetStudentById } from "@/kubb";
import { GraduationCap } from "lucide-react";
import { useState } from "react";
import { useParams } from "react-router-dom";
import { StudentSummary } from "../components/StudentSummary";

export function StudentDetailsPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";
  const [currentPage, setCurrentPage] = useState(0);

  const {
    isPending: isStudentPending,
    data: student,
    error: studentError,
  } = useGetStudentById(studentId);

  const {
    isPending: isStudentEventsPending,
    data: studentEvents,
    error: studentEventsError,
  } = useEventsByStudent(studentId);

  return (
    <>
      <PageHeader
        description="Veja e gerencie as informações do aluno"
        title="Detalhes do aluno"
        Icon={GraduationCap}
        backLink="/students"
      />

      <div className="grid gap-3 animate-[fade-up_300ms_ease-out_both]">
        <StudentSummary
          student={student}
          isPending={isStudentPending}
          error={studentError}
        />

        <EventsTable
          eventsPage={studentEvents}
          isPending={isStudentEventsPending}
          error={studentEventsError}
          currentPage={currentPage}
          onPageChange={setCurrentPage}
        />
      </div>
    </>
  );
}
