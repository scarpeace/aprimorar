import { ErrorCard } from "@/components/ui/error-card";
import { PageHeader } from "@/components/ui/page-header";
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

  const {
    data: parent,
    isError: isParentError,
    isPending: isParentPending,
    error: parentError,
  } = useGetParentById(parentId);

  const {
    data: parentStudents,
    isPending: isParentStudentsPending,
    error: parentStudentsError,
  } = useGetStudentsByParent(parentId);

  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Nome completo", value: parent?.name },
    { label: "CPF", value: formatCpf(parent?.cpf ?? "") },
    { label: "E-mail", value: parent?.email },
    { label: "Contato", value: formatPhone(parent?.contact ?? "") },
    { label: "Criado em", value: formatDateShortYear(parent?.createdAt ?? "") },
    { label: "Status", value: parent?.archivedAt ? "Arquivado" : "Ativo" },
  ];

  if (isParentError) {
    return (
      <ErrorCard title="Erro ao carregar responsável" error={parentError} />
    );
  }

  if (isParentPending) {
    return <LoadingCard title="Carregando dados do responsável" />;
  }

  return (
    <>
      <PageHeader
        description="Veja e gerencie as informações do responsável"
        title="Detalhes do Responsável"
        Icon={Handshake}
        backLink={"/parents"}
      />

      <div className="flex flex-col">
        <SectionCard
          title="Resumo do responsável"
          description="Dados do responsável"
          headerActions={
            <>
              <ButtonLink to={`/parents/edit/${parentId}`} variant="primary">
                <Edit className="h-4 w-4" />
                Editar
              </ButtonLink>

              <ArchiveParentButton
                parentId={parentId}
                isArchived={!!parent.archivedAt}
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
        <StudentsTable
          students={parentStudents}
          onPageChange={setCurrentPage}
          currentPage={currentPage}
          isPending={isParentStudentsPending}
          error={parentStudentsError}
        />
      </div>
    </>
  );
}
