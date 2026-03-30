import type { ReactNode } from "react";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { formatCpf, formatDateShortYear, formatPhone } from "@/lib/utils/formatter";
import type { StudentResponseSchema } from "../hooks/studentSchema";

type StudentSummarySectionProps = {
  student: StudentResponseSchema;
};

export function StudentSummarySection({
  student,
}: Readonly<StudentSummarySectionProps>) {
  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Nome completo", value: student?.name },
    { label: "CPF", value: formatCpf(student?.cpf ?? "") },
    { label: "E-mail", value: student?.email },
    { label: "Idade", value: student?.age },
    { label: "Contato", value: formatPhone(student?.contact ?? "") },
    { label: "Data de matrícula", value: formatDateShortYear(student?.createdAt ?? "") },
    { label: "Escola", value: student?.school },
    { label: "Status", value: student?.archivedAt ? "Arquivado" : "Ativo" },
    { label: "Responsável", value: student?.parent?.name },
  ];

  return (
    <SectionCard
      title="Resumo do aluno"
      description="Dados de aluno, responsável e endereço em um único resumo."
    >
      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
        {summaryItems.map((item) => (
          <SummaryItem key={item.label} label={item.label} value={item.value} />
        ))}
      </div>
    </SectionCard>
  );
}
