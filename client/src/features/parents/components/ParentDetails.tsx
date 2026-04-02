import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import type { ParentResponseDTO } from "@/kubb";
import { formatCpf, formatPhone, formatDateShortYear } from "@/lib/utils/formatter";
import type { ReactNode } from "react";

type ParentDetailsProps = {
  parent: ParentResponseDTO;
};

export function ParentDetails({
  parent,
}: Readonly<ParentDetailsProps>) {
  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Nome completo", value: parent?.name },
    { label: "CPF", value: formatCpf(parent?.cpf ?? "") },
    { label: "E-mail", value: parent?.email },
    { label: "Contato", value: formatPhone(parent?.contact ?? "") },
    { label: "Criado em", value: formatDateShortYear(parent?.createdAt ?? "") },
    { label: "Status", value: parent?.archivedAt ? "Arquivado" : "Ativo" },
  ];

  return (
    <SectionCard
      title="Resumo do Responsável"
      description="Dados de Responsável, e alunos vinculados e ele(a)."
    >
      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
        {summaryItems.map((item) => (
          <SummaryItem key={item.label} label={item.label} value={item.value} />
        ))}
      </div>
    </SectionCard>
  );
}
