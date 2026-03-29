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

export function StudentDetailPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";
  const navigate = useNavigate();
  const [currentPage, setCurrentPage] = useState(0);

  const {
    data: student,
    isLoading: isStudentLoading,
    error: studentError,
  } = useStudentByIdQuery({ studentId });

  const {
    data: studentEvents,
    isLoading: isStudentEventsLoading,
    error: studentEventsError,
  } = useEventsByStudent(studentId);

  if (isStudentLoading) {
    return <PageLoading message="Carregando aluno..." />;
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

      {studentError && (
        <PageError
          message="Ocorreu um erro ao carregar o aluno."
          error={studentError}
        />
      )}

      <div className="grid gap-2">
        <StudentSummarySection student={student} studentId={studentId} />
        <Collapse title="Endereço">
          <AddressSummarySection address={student?.address} />
        </Collapse>
      </div>

      <StudentEventsSection
        studentId={studentId}
        events={studentEvents}
        isLoading={isStudentEventsLoading}
        error={studentEventsError}
        currentPage={currentPage}
        onPageChange={setCurrentPage}
      />
    </div>
  );
}
