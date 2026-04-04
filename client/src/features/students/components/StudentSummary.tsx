import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import type { StudentResponseDTO } from "@/kubb";
import {
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "@/lib/utils/formatter";
import type { ReactNode } from "react";
import { ArchiveStudentButton } from "./ArchiveStudentButton";
import { DeleteStudentButton } from "./DeleteStudentButton";
import { ButtonLink } from "@/components/ui/button";
import { Edit } from "lucide-react";
import { AddressDetails } from "@/features/address/AddressDetails";
import { Collapse } from "@/components/ui/collapse";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";

type StudentSummaryProps = {
  student?: StudentResponseDTO;
  isPending: boolean;
  error: unknown;
};

export function StudentSummary({ student, isPending, error }: Readonly<StudentSummaryProps>) {
  if (error) {
    return <ErrorCard title="Erro ao carregar detalhes do aluno" error={error} />;
  }

  if (isPending || !student) {
    return <LoadingCard title="Carregando detalhes do aluno" />;
  }

  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Nome completo", value: student.name },
    { label: "CPF", value: formatCpf(student.cpf) },
    { label: "E-mail", value: student.email },
    { label: "Idade", value: student.age },
    { label: "Contato", value: formatPhone(student.contact) },
    {
      label: "Data de matrícula",
      value: formatDateShortYear(student.createdAt),
    },
    { label: "Escola", value: student.school },
    { label: "Status", value: student.archivedAt ? "Arquivado" : "Ativo" },
  ];

  return (
    <SectionCard
      title="Resumo do aluno"
      description="Dados do aluno"
      headerActions={
        <>
          <ButtonLink to={`/students/edit/${student.id}`} variant="primary">
            <Edit className="h-4 w-4" />
            Editar
          </ButtonLink>

          <ArchiveStudentButton
            studentId={student.id}
            isArchived={!!student.archivedAt}
          />
          <DeleteStudentButton studentId={student.id} />
        </>
      }
    >
      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
        {summaryItems.map((item) => (
          <SummaryItem key={item.label} label={item.label} value={item.value} />
        ))}
      </div>

      <Collapse title={"Endereço"} className="mt-3">
        <AddressDetails address={student.address} />
      </Collapse>
    </SectionCard>
  );
}
