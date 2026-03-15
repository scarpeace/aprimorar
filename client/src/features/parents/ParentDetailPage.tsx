import type { ReactNode } from "react"
import { UserCog } from "lucide-react"
import { useNavigate, useParams } from "react-router-dom"
import { ButtonLink } from "@/components/ui/button"
import { ErrorCard } from "@/components/ui/error-card"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { SectionCard } from "@/components/ui/section-card"
import { SummaryItem } from "@/components/ui/summary-item"
import styles from "./ParentDetailPage.module.css"
import { getFriendlyErrorMessage } from "@/services/api"
import { useParentDetailQuery, useStudentsByParentQuery, useArchiveParent, useUnarchiveParent, useDeleteParent } from "./hooks/use-parents"

import { StudentsTable } from "@/features/students/components/StudentsTable"
import { Alert } from "@/components/ui/alert"
import { DetailsPageActions } from "@/components/ui/details-page-actions"
export function ParentDetailPage() {
  const { id } = useParams<{ id: string }>()
  const parentId = id ?? ""
  const navigate = useNavigate()

  const { data: parentData, error: parentError, isLoading: isParentLoading } = useParentDetailQuery(parentId)
  const { data: parentStudentsData, error: parentStudentsError, isLoading: isParentStudentsLoading } = useStudentsByParentQuery(parentId)

  const { mutate: archiveParent, isPending: isArchivePending, isError: isArchiveError, error: archiveError } = useArchiveParent()
  const { mutate: unarchiveParent, isPending: isUnarchivePending, isError: isUnarchiveError, error: unarchiveError } = useUnarchiveParent()
  const { mutate: deleteParent, isPending: isDeletePending, isError: isDeleteError, error: deleteError } = useDeleteParent()

  const handleArchiveToggle = () => {
    if (parentData?.archivedAt) {
      unarchiveParent(parentId)
    } else {
      archiveParent(parentId)
    }
  }

  const handleParentDelete = () => {
    const confirmed = globalThis.confirm("Tem certeza que deseja excluir este responsável? ESTA AÇÃO NÃO PODE SER DESFEITA.")
    if (confirmed) {
      deleteParent(parentId)
    }
  }

  const mutationError = archiveError || unarchiveError || deleteError
  const isMutationError = isArchiveError || isUnarchiveError || isDeleteError

  if (isParentLoading || isParentStudentsLoading) {
    return <PageLoading message="Carregando responsável..." />
  }

  if (parentError || !parentData || parentStudentsError || !parentStudentsData) {
    const queryError = parentError ?? parentStudentsError
    return (
      <div className={styles.page}>
        <ErrorCard
          description={getFriendlyErrorMessage(queryError)}
          actionLabel="Voltar para listagem de responsáveis"
          onAction={() => navigate("/parents")} />
      </div>
    )
  }

  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Nome completo", value: parentData.name },
    { label: "E-mail", value: parentData.email },
    { label: "Contato", value: parentData.contact },
    { label: "CPF", value: parentData.cpf },
    { label: "Status", value: parentData.archivedAt ? "Arquivado" : "Ativo" },
    { label: "Criado em", value: parentData.createdAt },
  ]

  return (
    <div className={styles.page}>
      <PageHeader
        action={
          <ButtonLink to="/parents" variant="outline">
            Voltar para responsáveis
          </ButtonLink>
        }
        description="Veja e gerencie as informações do responsável"
        leading={<UserCog className="h-6 w-6 text-success" />}
        title="Detalhes do responsável"
        titleClassName="text-2xl font-bold app-text"
      />

      <SectionCard
        title="Resumo do responsável"
        description="Dados completos de cadastro, contato e status."
        headerAction={
          <DetailsPageActions
            data={parentData}
            editTo={`/parents/edit/${parentData.id}`}
            handleArchive={handleArchiveToggle}
            handleDelete={handleParentDelete}
            isArchivePending={isArchivePending || isUnarchivePending}
            isDeletePending={isDeletePending}
          />
        }
      >
        {isMutationError && (
          <Alert variant="error">
            {getFriendlyErrorMessage(mutationError)}
          </Alert>
        )}

        <div className={styles.summaryGrid}>
          {summaryItems.map((item) => (
            <SummaryItem key={item.label} label={item.label} value={item.value} />
          ))}
        </div>
      </SectionCard>

      <SectionCard
        title="Alunos vinculados"
        description={`Alunos registrados sob a responsabilidade de ${parentData.name}.`}
      >

        <StudentsTable
          students={parentStudentsData ?? []}
          loading={isParentStudentsLoading}
          error={parentStudentsError ? getFriendlyErrorMessage(parentStudentsError) : ""}
          variant="parentPage" />
      </SectionCard>
    </div>
  )
}
