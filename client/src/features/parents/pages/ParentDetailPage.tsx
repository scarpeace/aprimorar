import { ErrorCard } from "@/components/ui/error-card";
import { PageLayout } from "@/components/layout/PageLayout";
import { Edit, Handshake } from "lucide-react";
import { useParams } from "react-router-dom";

import { Button } from "@/components/ui/button";
import { LoadingCard } from "@/components/ui/loading-card";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { ArchiveParentButton } from "@/features/parents/components/ArchiveParentButton";
import { DeleteParentButton } from "@/features/parents/components/DeleteParentButton";
import { getActiveLinkedStudentsCount } from "@/features/parents/utils/getActiveLinkedStudentsCount";
import { StudentsTable } from "@/features/students/components/StudentsTable";
import { useGetParentById, useGetStudentsByParent } from "@/kubb";
import {
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "@/lib/utils/formatter";
import { useState } from "react";
import { ParentForm } from "../components/ParentForm";

export function ParentDetailPage() {
  const { id } = useParams<{ id: string }>();
  const parentId = id ?? "";
  const [currentPage, setCurrentPage] = useState(0);
  const [isFormOpen, setIsFormOpen] = useState(false);

  const parentQuery = useGetParentById(parentId);
  const linkedStudentsMetaQuery = useGetStudentsByParent(parentId, {
    page: 0,
    size: 1,
  });
  const parentStudentsQuery = useGetStudentsByParent(parentId, {
    page: currentPage,
  });
  const linkedStudentsCount = linkedStudentsMetaQuery.data?.totalElements ?? 0;
  const linkedStudentsSummaryQuery = useGetStudentsByParent(
    parentId,
    {
      page: 0,
      size: linkedStudentsCount,
    },
    {
      query: {
        enabled: !!parentId && linkedStudentsCount > 0,
      },
    },
  );
  const activeLinkedStudentsCount = getActiveLinkedStudentsCount(
    linkedStudentsSummaryQuery.data,
  );
  const archivedLinkedStudentsCount = Math.max(
    linkedStudentsCount - activeLinkedStudentsCount,
    0,
  );
  const hasActiveLinkedStudents = activeLinkedStudentsCount > 0;
  const hasArchivedLinkedStudents = archivedLinkedStudentsCount > 0;

  const headerProps = {
    description: "Veja os dados do responsável, os vínculos ativos e o histórico arquivado com alunos.",
    title: "Detalhes do Responsável",
    Icon: Handshake,
    backLink: "/parents",
  };

  if (
    parentQuery.isError ||
    parentStudentsQuery.isError ||
    linkedStudentsMetaQuery.isError ||
    linkedStudentsSummaryQuery.isError
  ) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard
          title="Erro ao carregar responsável"
          error={
            parentQuery.error ||
            parentStudentsQuery.error ||
            linkedStudentsMetaQuery.error ||
            linkedStudentsSummaryQuery.error
          }
        />
      </PageLayout>
    );
  }

  if (
    parentQuery.isPending ||
    parentStudentsQuery.isPending ||
    linkedStudentsMetaQuery.isPending ||
    linkedStudentsSummaryQuery.isPending
  ) {
    return (
      <PageLayout {...headerProps}>
        <LoadingCard title="Carregando dados do responsável" />
      </PageLayout>
    );
  }

  return (
    <PageLayout {...headerProps}>
      <div className="grid gap-3 animate-[fade-up_300ms_ease-out_both]">
        <SectionCard
          title="Responsável"
          description="Dados cadastrais e regras atuais de vínculo."
          headerActions={
            <div className="flex gap-2 items-center flex-wrap justify-end">
              <Button onClick={() => setIsFormOpen(true)} variant="primary">
                <Edit className="h-4 w-4 mr-2" />
                Editar
              </Button>

              <ArchiveParentButton
                parentId={parentId}
                isArchived={!!parentQuery.data?.archivedAt}
              />
              <DeleteParentButton parentId={parentId} />
            </div>
          }
        >
          <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-2">
            <SummaryItem label="Nome completo" value={parentQuery.data?.name} />
            <SummaryItem label="CPF" value={formatCpf(parentQuery.data?.cpf)} />
            <SummaryItem label="E-mail" value={parentQuery.data?.email} />
            <div className="flex gap-3">
              <SummaryItem
                className="grow"
                label="Contato"
                value={formatPhone(parentQuery.data?.contact)}
              />
              <SummaryItem
                label="Criado em"
                value={formatDateShortYear(parentQuery.data?.createdAt ?? "")}
              />
              <SummaryItem
                label="Status"
                value={parentQuery.data?.archivedAt ? "Arquivado" : "Ativo"}
              />
            </div>
          </div>

          <div className="rounded-xl border border-base-300 bg-base-200/60 p-4 text-sm text-base-content/80">
            {hasActiveLinkedStudents
              ? `Este responsável possui ${activeLinkedStudentsCount} aluno(s) ativo(s) vinculado(s). Enquanto esses vínculos estiverem ativos, ações de arquivar ou excluir podem ser bloqueadas para preservar a integridade do cadastro.`
              : hasArchivedLinkedStudents
                ? `Este responsável não possui vínculos ativos no momento, mas mantém ${archivedLinkedStudentsCount} aluno(s) apenas no histórico arquivado. Esse histórico continua disponível para consulta e não bloqueia a exclusão.`
                : "Este responsável não possui alunos vinculados no momento. O cadastro continua válido mesmo sem vínculo ativo."}
          </div>
        </SectionCard>

        <SectionCard
          title="Alunos vinculados"
          description="Relação de alunos ativos e histórico arquivado associados a este responsável."
        >
          <StudentsTable
            students={parentStudentsQuery.data}
            onPageChange={setCurrentPage}
            currentPage={currentPage}
            isPending={parentStudentsQuery.isPending}
            error={parentStudentsQuery.error}
          />
        </SectionCard>

        {isFormOpen && (
          <div className="modal modal-open">
            <div className="modal-box max-w-2xl">
              <h3 className="font-bold text-lg mb-4">Editar Responsável</h3>
              <ParentForm
                initialData={parentQuery.data}
                onSuccess={() => setIsFormOpen(false)}
                onCancel={() => setIsFormOpen(false)}
              />
            </div>
          </div>
        )}
      </div>
    </PageLayout>
  );
}
