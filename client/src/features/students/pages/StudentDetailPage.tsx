import { useState } from "react";
import { GraduationCap } from "lucide-react";
import { useParams, useNavigate } from "react-router-dom";
import { ButtonLink } from "@/components/ui/button";
import { PageHeader } from "@/components/ui/page-header";
import { AddressSummarySection } from "@/features/address/AddressSumarySection";
import { Collapse } from "@/components/ui/collapse";
import { useEventsByStudent } from "@/features/events/query/eventQueries";
import { useGetStudentById } from "@/kubb";
import { StudentDetailState } from "../components/StudentDetailState";
import { StudentEventsSection } from "../components/StudentEventsSection";
import { StudentSummarySection } from "../components/StudentSummarySection";
import { useStudentByIdQuery } from "../hooks/use-students-query";
import { ErrorCard } from "@/components/ui/error-card";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { PageLoading } from "@/components/ui/page-loading";
import { Alert } from "@/components/ui/alert";
import { PageError } from "@/components/ui/page-error";
import { LoadingCard } from "@/components/ui/loading-card";

export function StudentDetailPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";
  const navigate = useNavigate();
  const [currentPage, setCurrentPage] = useState(0);

  const studentQuery = useStudentByIdQuery({ studentId });

  const studentEventsQuery = useEventsByStudent(studentId);

  if (studentQuery.isError) {
    return <ErrorCard title="Erro ao carregar aluno" error={studentQuery.error} />
  }

  if (studentQuery.isPending) {
    return <LoadingCard title="Carregando listagem de alunos" />
  }

  return (
    <div className="animate-[fade-up_300ms_ease-out_both] flex flex-col gap-7">
      <PageHeader
        description="Veja e gerencie as informações do aluno"
        title="Detalhes do aluno"
        Icon={GraduationCap}
        action={
          <ButtonLink className="sm:ml-auto" to="/students" variant="outline">
            Voltar para alunos
          </ButtonLink>
        }
      />

      <div className="grid gap-2">
        <StudentSummarySection student={studentQuery.data} studentId={studentId} />
        <Collapse title="Endereço">
          <AddressSummarySection address={studentQuery.data.address} />
        </Collapse>
      </div>

      <StudentEventsSection
        studentId={studentId}
        events={studentEventsQuery.data?.content}
        isLoading={studentEventsQuery.isPending}
        error={studentEventsQuery.error}
        currentPage={currentPage}
        onPageChange={setCurrentPage}
      />
    </div>
  );
}
