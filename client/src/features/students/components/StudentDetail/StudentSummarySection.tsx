import type { StudentResponseDTO } from "@/kubb";
import type { ReactNode } from "react";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { formatDateShortYear } from "@/lib/utils/formatter";
import { ArchiveStudentButton } from "../ArchiveStudentButton";
import { DeleteStudentButton } from "../DeleteStudentButton";
import { EditStudentButton } from "../EditStudentButton";

type StudentSummarySectionProps = {
  student?: StudentResponseDTO;
  studentId: string;
};

export function StudentSummarySection({
  student,
  studentId,
}: Readonly<StudentSummarySectionProps>) {
  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Nome completo", value: student?.name },
    { label: "CPF", value: student?.cpf },
    { label: "E-mail", value: student?.email },
    { label: "Idade", value: student?.age },
    { label: "Contato", value: student?.contact },
    //TODO: pq esse campo abaixo tem que ser diferente?
    { label: "Data de matrícula", value: formatDateShortYear(student?.createdAt ?? "") },
    { label: "Escola", value: student?.school },
    { label: "Status", value: student?.archivedAt ? "Arquivado" : "Ativo" },
    { label: "Responsável", value: student?.parent?.name },
  ];

  return (
    <SectionCard
      title="Resumo do aluno"
      description="Dados de aluno, responsável e endereço em um único resumo."
      headerAction={
        <>
          <EditStudentButton studentId={studentId} />
          <ArchiveStudentButton studentId={studentId} isArchived={!!student?.archivedAt} />
          <DeleteStudentButton studentId={studentId} />
        </>
      }
    >
      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
        {summaryItems.map((item) => (
          <SummaryItem key={item.label} label={item.label} value={item.value} />
        ))}
      </div>
    </SectionCard>
  );
}
