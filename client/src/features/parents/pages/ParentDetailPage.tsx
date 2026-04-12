import { ErrorCard } from "@/components/ui/error-card";
import { PageLayout } from "@/components/layout/PageLayout";
import { Edit, Handshake } from "lucide-react";
import { useParams } from "react-router-dom";

import { ButtonLink } from "@/components/ui/button";
import { LoadingCard } from "@/components/ui/loading-card";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { ArchiveParentButton } from "@/features/parents/components/ArchiveParentButton";
import { DeleteParentButton } from "@/features/parents/components/DeleteParentButton";
import { StudentsTable } from "@/features/students/components/StudentsTable";
import { useGetParentById, useGetStudentsByParent } from "@/kubb";
import {
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "@/lib/utils/formatter";
import { useState, type ReactNode } from "react";

export function ParentDetailPage() {
  const { id } = useParams<{ id: string }>();
  const parentId = id ?? "";
  const [currentPage, setCurrentPage] = useState(0);

  const parentQuery = useGetParentById(parentId);
  const parentStudentsQuery = useGetStudentsByParent(parentId);

  const headerProps = {
    description: "Veja e gerencie as informações do responsável",
    title: "Detalhes do Responsável",
    Icon: Handshake,
    backLink: "/parents",
  };

  if (parentQuery.isError || parentStudentsQuery.isError) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard title="Erro ao carregar responsável" error={parentQuery.error || parentStudentsQuery.error} />
      </PageLayout>
    );
  }

  if (parentQuery.isPending || parentStudentsQuery.isPending) {
    return (
      <PageLayout {...headerProps}>
        <LoadingCard title="Carregando dados do responsável" />
      </PageLayout>
    );
  }

  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Nome completo", value: parentQuery.data?.name },
    { label: "CPF", value: formatCpf(parentQuery.data?.cpf) },
    { label: "E-mail", value: parentQuery.data?.email },
    { label: "Contato", value: formatPhone(parentQuery.data?.contact) },
    { label: "Criado em", value: formatDateShortYear(parentQuery.data?.createdAt ?? "") },
    { label: "Status", value: parentQuery.data?.archivedAt ? "Arquivado" : "Ativo" },
  ];

  return (
    <PageLayout {...headerProps}>
      <div className="grid gap-3 animate-[fade-up_300ms_ease-out_both]">
        <SectionCard
          title="Responsável"
          description="Dados do responsável"
          headerActions={
            <>
              <ButtonLink to={`/parents/edit/${parentId}`} variant="primary">
                <Edit className="h-4 w-4" />
                Editar
              </ButtonLink>

              <ArchiveParentButton
                parentId={parentId}
                isArchived={!!parentQuery.data?.archivedAt}
              />
              <DeleteParentButton parentId={parentId} />
            </>
          }
        >
          <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-2">
            {summaryItems.map((item) => (
              <SummaryItem
                key={item.label}
                label={item.label}
                value={item.value}
              />
            ))}
          </div>
        </SectionCard>
        <SectionCard
          title={"Alunos"}
          description={"Alunos vinculados ao responsável"}
        >
          <StudentsTable
            students={parentStudentsQuery.data}
            onPageChange={setCurrentPage}
            currentPage={currentPage}
            isPending={parentStudentsQuery.isPending}
            error={parentStudentsQuery.error}
          />
        </SectionCard>
      </div>
    </PageLayout>
  );
}
