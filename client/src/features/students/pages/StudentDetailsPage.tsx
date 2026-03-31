import { Collapse } from "@/components/ui/collapse";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { PageHeader } from "@/components/ui/page-header";
import { Pagination } from "@/components/ui/pagination";
import { AddressSummarySection } from "@/features/address/AddressSumarySection";
import { EventsTable } from "@/features/events/components/EventsTable";
import { useEventsByStudent } from "@/features/events/query/eventQueries";
import { GraduationCap } from "lucide-react";
import { useState } from "react";
import { useParams } from "react-router-dom";
import { ArchiveStudentButton } from "../components/ArchiveStudentButton";
import { DeleteStudentButton } from "../components/DeleteStudentButton";
import { EditStudentButton } from "../components/EditStudentButton";
import { StudentDetails } from "../components/StudentDetails";
import { useStudentByIdQuery } from "../hooks/use-students-query";

export function StudentDetailsPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";
  const [currentPage, setCurrentPage] = useState(0);

  const {
    data: student,
    isError: isStudentError,
    isPending: isStudentPending,
    error: studentError,
  } = useStudentByIdQuery({ studentId });
  const {
    data: pagedStudentEvents,
    isPending: isStudentEventsPending,
    error: studentEventsError,
  } = useEventsByStudent(studentId);

  const { content: studentEvents, ...pageMetadata } = pagedStudentEvents || {};

  if (isStudentError) {
    return <ErrorCard title="Erro ao carregar aluno" error={studentError} />;
  }

  if (isStudentPending) {
    return <LoadingCard title="Carregando dados do aluno" />;
  }

  return (
    <>
      <PageHeader
        description="Veja e gerencie as informações do aluno"
        title="Detalhes do aluno"
        Icon={GraduationCap}
      >
        <div className="flex flex-row ml-auto mt-auto gap-3">
          <EditStudentButton studentId={studentId} />
          <ArchiveStudentButton
            studentId={studentId}
            isArchived={!!student.archivedAt}
          />
          <DeleteStudentButton studentId={studentId} />
        </div>
      </PageHeader>

      <div className="grid gap-3 animate-[fade-up_300ms_ease-out_both]">
        <StudentDetails student={student} />
        <Collapse title={"Endereço"}>
          <AddressSummarySection address={student.address} />
        </Collapse>

        <EventsTable
          events={studentEvents}
          isPending={isStudentEventsPending}
          error={studentEventsError}
        >
          <Pagination
            paginationData={pageMetadata.page}
            currentPage={currentPage}
            onPageChange={setCurrentPage}
            currentSize={studentEvents?.length}
          />
        </EventsTable>
      </div>
    </>
  );
}
