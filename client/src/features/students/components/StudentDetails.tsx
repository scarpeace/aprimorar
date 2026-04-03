import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import type { StudentResponseDTO } from "@/kubb";
import { formatCpf, formatDateShortYear, formatPhone } from "@/lib/utils/formatter";
import type { ReactNode } from "react";
import { ArchiveStudentButton } from "./ArchiveStudentButton";
import { DeleteStudentButton } from "./DeleteStudentButton";
import { EditStudentButton } from "./EditStudentButton";

type StudentDetailsProps = {
  student: StudentResponseDTO;
};

export function StudentDetails({
  student,
}: Readonly<StudentDetailsProps>) {
  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Nome completo", value: student?.name },
    { label: "CPF", value: formatCpf(student?.cpf ?? "") },
    { label: "E-mail", value: student?.email },
    { label: "Idade", value: student?.age },
    { label: "Contato", value: formatPhone(student?.contact ?? "") },
    { label: "Data de matrícula", value: formatDateShortYear(student?.createdAt ?? "") },
    { label: "Escola", value: student?.school },
    { label: "Status", value: student?.archivedAt ? "Arquivado" : "Ativo" },
  ];

  return (
      <SectionCard
      title="Resumo do aluno"
      description="Dados de aluno, responsável e endereço em um único resumo."
    >
        <EditStudentButton studentId={student.id} />
          <ArchiveStudentButton
            studentId={student.id}
            isArchived={!!student.archivedAt}
          />
          <DeleteStudentButton studentId={student.id} />

<div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
        {summaryItems.map((item) => (
          <SummaryItem key={item.label} label={item.label} value={item.value} />
        ))}
      </div>
    </SectionCard>
  );
}
